package ru.clevertec.ecl.exception;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class TagValidationException extends AbstractValidationException {

    public TagValidationException(Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(constraintViolations);
    }

    @Override
    public int getCode() {
        return 40001;
    }
}
