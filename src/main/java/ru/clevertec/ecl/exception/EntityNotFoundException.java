package ru.clevertec.ecl.exception;

public abstract class EntityNotFoundException extends AbstractServiceException {

    public EntityNotFoundException(Long id) {
        super(String.format("Requested resource not found (id = %d)", id));
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
