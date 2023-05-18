package ru.clevertec.ecl.data.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ResponseTagDto(

        @Schema(defaultValue = "1")
        Long id,

        @Schema(defaultValue = "Beauty")
        String name) {
}
