package ru.clevertec.ecl.exception;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class GiftCertificateValidationException extends AbstractValidationException {

    public GiftCertificateValidationException(Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(constraintViolations);
    }

    @Override
    public int getCode() {
        return 40002;
    }
}
