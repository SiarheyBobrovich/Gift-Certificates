package ru.clevertec.ecl.data.user;

import ru.clevertec.ecl.data.order.ResponseOrderDto;

import java.util.List;

public record ResponseUserDto(
        Long id,
        String firstName,
        String lastName,
        List<ResponseOrderDto> orders) {
}
