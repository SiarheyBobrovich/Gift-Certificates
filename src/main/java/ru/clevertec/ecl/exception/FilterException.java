package ru.clevertec.ecl.exception;

import lombok.Getter;

@Getter
public class FilterException extends RuntimeException {

    private final int code = 40002;

    public FilterException(String s) {
        super(s);
    }
}
