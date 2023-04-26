package ru.clevertec.ecl.exception;

public abstract class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Long id) {
        super(String.format("Requested resource not found (id = %d)", id));
    }

    public abstract int getCode();
}
