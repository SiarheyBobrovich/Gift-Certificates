package ru.clevertec.ecl.controller.handler;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.ecl.exception.AbstractServiceException;
import ru.clevertec.ecl.exception.ResponseError;

@Log4j2
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseError> handleEntityNotFoundException(AbstractServiceException e) {
        log.info(e.getMessage());
        ResponseError responseError = new ResponseError(e.getMessage(), e.getCode());

        return ResponseEntity.badRequest().body(responseError);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseError> handleConstraintViolationException(ConstraintViolationException e) {
        log.info(e.getConstraintName());
        ResponseError responseError = new ResponseError(e.getMessage(), 409);

        return ResponseEntity.badRequest().body(responseError);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseError> handleThrowable(Throwable e) {
        log.info(e.getMessage());
        ResponseError responseError = new ResponseError("Please try again letter", 500);

        return ResponseEntity.internalServerError().body(responseError);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseError> handleThrowable(MethodArgumentNotValidException e) {
        String message = e.getFieldErrors().stream()
                .map(error -> StringUtils.joinWith(" = ", error.getField(), error.getDefaultMessage()))
                .reduce((s1, s2) -> StringUtils.joinWith("; ", s1, s2))
                .orElse("Bad Request");
        log.info(message);
        ResponseError responseError = new ResponseError(message, 400);

        return ResponseEntity.badRequest().body(responseError);
    }
}
