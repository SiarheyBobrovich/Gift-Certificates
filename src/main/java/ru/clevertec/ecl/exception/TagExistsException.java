package ru.clevertec.ecl.exception;

public class TagExistsException extends AbstractServiceException {

    public TagExistsException(String name) {
        super(String.format("Tag with name: %s already exists", name));
    }

    @Override
    public int getCode() {
        return 40001;
    }
}
