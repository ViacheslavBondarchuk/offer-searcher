package io.github.viacheslavbondarchuk.offersearcher.exception;

import java.io.Serial;

public final class SearchServiceNotReadyException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5394787745031429269L;

    public SearchServiceNotReadyException(String message) {
        super(message);
    }
}
