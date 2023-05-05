package ru.clevertec.ecl.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.ecl.exception.AbstractServiceException;
import ru.clevertec.ecl.exception.AbstractValidationException;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.exception.FilterException;

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

    record Error(String errorMessage, int errorCode) {
    }
}
