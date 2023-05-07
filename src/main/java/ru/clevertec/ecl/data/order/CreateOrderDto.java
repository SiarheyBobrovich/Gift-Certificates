package ru.clevertec.ecl.data.order;

import jakarta.validation.constraints.NotNull;

public record CreateOrderDto(

        @NotNull
        Long userId,

        @NotNull
        Long certificateId
) {
}
