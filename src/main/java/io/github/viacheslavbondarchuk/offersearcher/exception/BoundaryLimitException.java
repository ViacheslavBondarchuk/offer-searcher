package io.github.viacheslavbondarchuk.offersearcher.exception;

import java.io.Serial;

public final class BoundaryLimitException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7720400298845278539L;

    public BoundaryLimitException(String message) {
        super(message);
    }
}
