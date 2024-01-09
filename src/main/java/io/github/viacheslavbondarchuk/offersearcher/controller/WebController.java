package io.github.viacheslavbondarchuk.offersearcher.controller;

import io.github.viacheslavbondarchuk.offersearcher.domain.ErrorResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

public interface WebController {

    @ExceptionHandler(Exception.class)
    default ErrorResponse exceptionHandler(Exception ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    default ErrorResponse validationExceptionHandler(MethodArgumentNotValidException ex) {
        StringBuilder builder = new StringBuilder("Validation errors: ");
        builder.append(ex.getAllErrors()
                .stream()
                .map(FieldError.class::cast)
                .map(error -> "%s: %s".formatted(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(", ", "{", "}")));
        return new ErrorResponse(builder.toString());
    }
}
