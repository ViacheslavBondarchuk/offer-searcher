package io.github.viacheslavbondarchuk.offersearcher.service;


import io.github.viacheslavbondarchuk.offersearcher.exception.AuthorizationException;
import io.github.viacheslavbondarchuk.offersearcher.util.Checking;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.function.Predicate;

import static io.github.viacheslavbondarchuk.offersearcher.constants.Headers.SECRET_KEY;

@Service
public class AuthorizationService {
    public static final String AUTHORIZATION_EXCEPTION_TEMPLATE = "Authorization exception. {0} header is absent";

    private final String secretKey;

    public AuthorizationService(@Value("${authorization.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    public void check(HttpServletRequest request) {
        String headerValue = request.getHeader(SECRET_KEY);
        Checking.check(headerValue, Objects::isNull,
                () -> new AuthorizationException(MessageFormat.format(AUTHORIZATION_EXCEPTION_TEMPLATE, SECRET_KEY)));
        Checking.check(headerValue, Predicate.not(secretKey::equals), () -> new AuthorizationException("Unauthorized"));
    }

}
