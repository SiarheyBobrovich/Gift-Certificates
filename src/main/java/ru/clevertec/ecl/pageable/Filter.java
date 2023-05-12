package ru.clevertec.ecl.pageable;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Filter {

    @NotEmpty
    private final String tag;

    @NotEmpty
    private final String part;
}
