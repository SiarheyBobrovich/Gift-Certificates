package ru.clevertec.ecl.exception;

public abstract class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Long id) {
        super(String.format("Id '%d' not found", id));
    }

    public abstract int getCode();
}
