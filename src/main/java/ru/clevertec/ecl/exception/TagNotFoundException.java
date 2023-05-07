package ru.clevertec.ecl.exception;

public class TagNotFoundException extends EntityNotFoundException {

    public TagNotFoundException(Long id) {
        super(id);
    }

    public TagNotFoundException() {
        super("Tag not found yet");
    }

    @Override
    public int getCode() {
        return 404001;
    }
}
