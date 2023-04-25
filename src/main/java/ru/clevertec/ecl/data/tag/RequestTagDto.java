package ru.clevertec.ecl.data.tag;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record RequestTagDto(

        @NotBlank
        @Pattern(regexp = "^#\\w+", message = "must start with '#'")
        @Size(max = 20)
        String name
) {
}
