package ru.clevertec.ecl.pageable;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Builder
@Getter
public class Filter {

    private final String tag;
    private final String part;

    public boolean isTag() {
        return Objects.nonNull(tag);
    }

    public boolean isPart() {
        return Objects.nonNull(part);
    }

    public String getPart() {
        return "%" + part + "%";
    }
}
