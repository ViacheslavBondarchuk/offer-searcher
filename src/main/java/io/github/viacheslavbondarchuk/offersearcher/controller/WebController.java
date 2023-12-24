package io.github.viacheslavbondarchuk.offersearcher.controller;

import io.github.viacheslavbondarchuk.offersearcher.domain.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;

public interface WebController {

    @ExceptionHandler(Exception.class)
    default ErrorResponse exceptionHandler(Exception ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
