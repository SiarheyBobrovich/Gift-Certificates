package ru.clevertec.ecl.exception;

public class FilterException extends AbstractServiceException {

    public FilterException(String s) {
        super(s);
    }

    @Override
    public int getCode() {
        return 40002;
    }
}
