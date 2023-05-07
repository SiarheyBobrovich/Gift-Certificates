package ru.clevertec.ecl.pageable;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Filter {

    private final String tag;
    private final String part;
}
