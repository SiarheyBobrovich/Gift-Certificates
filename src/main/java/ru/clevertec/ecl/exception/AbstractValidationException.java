package ru.clevertec.ecl.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;

public abstract class AbstractValidationException extends ConstraintViolationException {
    public AbstractValidationException(Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(constraintViolations);
    }

    public abstract int getCode();
}
