package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.data.order.CreateOrderDto;
import ru.clevertec.ecl.data.order.ResponseOrderDto;

public interface OrderService {

    void create(CreateOrderDto createOrderDto);

    Page<ResponseOrderDto> findAllByUserId(Long userId, Pageable pageable);
}
