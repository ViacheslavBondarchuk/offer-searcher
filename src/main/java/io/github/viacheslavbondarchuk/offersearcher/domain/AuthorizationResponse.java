package io.github.viacheslavbondarchuk.offersearcher.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@JsonInclude(value = NON_NULL)
public final class AuthorizationResponse {
    private final String sessionToken;

    public AuthorizationResponse(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public static AuthorizationResponse of(String sessionToken) {
        return new AuthorizationResponse(sessionToken);
    }

}
