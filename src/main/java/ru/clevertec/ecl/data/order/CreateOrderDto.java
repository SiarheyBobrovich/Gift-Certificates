package ru.clevertec.ecl.data.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CreateOrderDto(

        @NotNull
        @Schema(defaultValue = "1")
        Long userId,

        @NotNull
        @Schema(defaultValue = "1")
        Long certificateId
) {
}
