package io.github.viacheslavbondarchuk.offersearcher.exception;

import java.io.Serial;

public final class AuthorizationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4095540884167817805L;

    public AuthorizationException(String message) {
        super(message);
    }
}
