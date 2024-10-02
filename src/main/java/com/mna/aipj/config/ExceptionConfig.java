package com.mna.aipj.config;

import com.mna.aipj.dto.Error;
import com.mna.aipj.dto.exception.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionConfig {

    @ExceptionHandler(value = {UserException.class})
    protected ResponseEntity<Error> handleSmileUserException(UserException ex, WebRequest request) {
        Error error = Error.builder()
                .reason(ex.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .error(ex.getStatus().name())
                .status(ex.getStatus().value())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();

        return new ResponseEntity<>(error, ex.getStatus());
    }
}
