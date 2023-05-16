package ru.clevertec.ecl.data.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestTagDto(

        @NotBlank
        @Size(max = 20)
        @Pattern(regexp = "^\\w+")
        @Schema(example = "Beauty")
        String name
) {
}
