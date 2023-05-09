package ru.clevertec.ecl.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.ecl.exception.AbstractServiceException;
import ru.clevertec.ecl.exception.AbstractValidationException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<Error> handleEntityNotFoundException(AbstractServiceException e) {
        log.info(e.getMessage());
        Error error = new Error(e.getMessage(), e.getCode());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler
    public ResponseEntity<Error> handleConstraintViolationException(ConstraintViolationException e) {
        log.info(e.getConstraintName());
        Error error = new Error(e.getMessage(), 409);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler
    public ResponseEntity<List<Error>> handleConstraintViolationException(AbstractValidationException e) {
        List<Error> errors = e.getConstraintViolations().stream()
                .map(error -> new Error(error.getMessage(), e.getCode()))
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler
    public ResponseEntity<Error> handleThrowable(Throwable e) {
        log.info(e.getMessage());
        Error error = new Error("Please try again letter", 500);
        return ResponseEntity.internalServerError().body(error);
    }

    @ExceptionHandler
    public ResponseEntity<Error> handleThrowable(MethodArgumentNotValidException e) {
        String message = e.getFieldErrors().stream()
                .map(error -> StringUtils.joinWith(" = ", error.getField(), error.getDefaultMessage()))
                .reduce((s1, s2)-> StringUtils.joinWith("; ", s1, s2))
                .orElse("Bad Request");
        log.info(message);
        Error error = new Error(message, 400);
        return ResponseEntity.badRequest().body(error);
    }

    record Error(String errorMessage, int errorCode) {
    }
}
