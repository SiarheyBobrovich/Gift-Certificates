package ru.clevertec.ecl.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.ecl.exception.EntityNotFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler()
    public ResponseEntity<Error> handleEntityNotFoundException(EntityNotFoundException e) {
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
    public ResponseEntity<Error> handleConstraintViolationException(Throwable e) {
        log.info(e.getMessage());
        Error error = new Error(e.getMessage(), 500);
        return ResponseEntity.badRequest().body(error);
    }

    record Error(String errorMessage, int errorCode) {
    }
}
