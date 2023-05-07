package ru.clevertec.ecl.data.order;

public record CreateOrderDto(
        Long userId,
        Long certificateId
) {
}
