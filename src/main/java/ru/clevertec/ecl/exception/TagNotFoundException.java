package ru.clevertec.ecl.exception;

public class TagNotFoundException extends EntityNotFoundException {

    public TagNotFoundException(Long id) {
        super(id);
    }

    @Override
    public int getCode() {
        return 404001;
    }
}
