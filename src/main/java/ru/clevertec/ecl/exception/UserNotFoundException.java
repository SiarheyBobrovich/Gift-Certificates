package ru.clevertec.ecl.exception;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(Long id) {
        super(id);
    }

    @Override
    public int getCode() {
        return 40003;
    }
}
