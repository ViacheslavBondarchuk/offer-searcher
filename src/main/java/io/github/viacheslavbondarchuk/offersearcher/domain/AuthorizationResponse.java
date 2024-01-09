package io.github.viacheslavbondarchuk.offersearcher.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Arrays;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@JsonInclude(value = NON_NULL)
public final class AuthorizationResponse {
    private final String sessionToken;
    private final Boolean success;

    public AuthorizationResponse(String sessionToken, boolean success) {
        this.sessionToken = sessionToken;
        this.success = success;
    }

    public static AuthorizationResponse success(String sessionToken) {
        return new AuthorizationResponse(sessionToken, true);
    }

    public static AuthorizationResponse checked() {
        return new AuthorizationResponse(null, true);
    }

}
