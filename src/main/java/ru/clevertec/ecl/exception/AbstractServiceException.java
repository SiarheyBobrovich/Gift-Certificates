package ru.clevertec.ecl.exception;

public abstract class AbstractServiceException extends RuntimeException {

    public AbstractServiceException(String message) {
        super(message);
    }

    public abstract int getCode();
}
