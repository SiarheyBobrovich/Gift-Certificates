package ru.clevertec.ecl.data.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestTagDto(

        @NotBlank
        @Pattern(regexp = "^\\w+")
        @Size(max = 20)
        String name
) {
}
