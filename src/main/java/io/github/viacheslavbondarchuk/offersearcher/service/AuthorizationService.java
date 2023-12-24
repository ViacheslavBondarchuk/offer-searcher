package io.github.viacheslavbondarchuk.offersearcher.service;


import com.hazelcast.map.IMap;
import io.github.viacheslavbondarchuk.offersearcher.domain.HazelcastSession;
import io.github.viacheslavbondarchuk.offersearcher.exception.AuthorizationException;
import io.github.viacheslavbondarchuk.offersearcher.util.Checking;
import io.github.viacheslavbondarchuk.offersearcher.util.TokenGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.github.viacheslavbondarchuk.offersearcher.constants.Headers.SECRET_KEY;
import static io.github.viacheslavbondarchuk.offersearcher.constants.Headers.SESSION_TOKEN;
import static io.github.viacheslavbondarchuk.offersearcher.constants.Headers.X_REAL_IP;
import static java.util.concurrent.TimeUnit.MINUTES;

@Service
public class AuthorizationService {
    public static final String AUTHORIZATION_EXCEPTION_TEMPLATE = "Authorization exception. {0} header is absent";
    private final IMap<String, HazelcastSession> hazelcastSessionMap;
    private final String secretKey;

    public AuthorizationService(IMap<String, HazelcastSession> hazelcastSessionMap,
                                @Value("${authorization.secret-key}") String secretKey) {
        this.hazelcastSessionMap = hazelcastSessionMap;
        this.secretKey = secretKey;
    }

    public String authorize(HttpServletRequest request) {
        String secretKeyHeader = request.getHeader(SECRET_KEY);
        String xRealIpHeader = request.getHeader(X_REAL_IP);
        Checking.check(secretKeyHeader, Objects::isNull, () -> new AuthorizationException(MessageFormat.format(AUTHORIZATION_EXCEPTION_TEMPLATE, SECRET_KEY)));
        Checking.check(xRealIpHeader, Objects::isNull, () -> new AuthorizationException(MessageFormat.format(AUTHORIZATION_EXCEPTION_TEMPLATE, X_REAL_IP)));
        Checking.check(secretKeyHeader, Predicate.not(secretKey::equals), () -> new AuthorizationException("Authorization exception"));
        long issueAt = System.currentTimeMillis();
        HazelcastSession hazelcastSession = new HazelcastSession(request.getRemoteAddr(), issueAt, issueAt + MINUTES.toMillis(30));
        String sessionToken = TokenGenerator.generate(hazelcastSession);
        hazelcastSessionMap.put(sessionToken, hazelcastSession, 30, MINUTES);
        return sessionToken;
    }

    public <T> T proceed(HttpServletRequest request, Supplier<T> supplier) {
        String sessionToken = request.getHeader(SESSION_TOKEN);
        String xRealIpHeader = request.getHeader(X_REAL_IP);
        Checking.check(sessionToken, Objects::isNull, () ->  new AuthorizationException("Unauthorized"));
        Checking.check(xRealIpHeader, Objects::isNull, () -> new AuthorizationException(MessageFormat.format(AUTHORIZATION_EXCEPTION_TEMPLATE, X_REAL_IP)));
        Checking.check(sessionToken, Predicate.not(hazelcastSessionMap::containsKey), () -> new AuthorizationException("Unauthorized"));
        HazelcastSession hazelcastSession = hazelcastSessionMap.get(sessionToken);
        hazelcastSessionMap.put(sessionToken, new HazelcastSession(request.getRemoteAddr(),
                hazelcastSession.getIssueAt(), System.currentTimeMillis() + MINUTES.toMillis(30)), 30, MINUTES);
        return supplier.get();
    }

}
