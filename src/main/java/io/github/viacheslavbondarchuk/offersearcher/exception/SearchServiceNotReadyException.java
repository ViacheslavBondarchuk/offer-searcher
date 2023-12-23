package io.github.viacheslavbondarchuk.offersearcher.exception;

public class SearchServiceNotReadyException extends RuntimeException {
    public SearchServiceNotReadyException(String message) {
        super(message);
    }
}
