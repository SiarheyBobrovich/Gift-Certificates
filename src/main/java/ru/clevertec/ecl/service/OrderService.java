package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.data.order.CreateOrderDto;
import ru.clevertec.ecl.data.order.ResponseOrderDto;

public interface OrderService {

    /**
     * Save a new Order
     *
     * @param createOrderDto dto must contain user ID and gift-certificate ID
     */
    ResponseOrderDto create(CreateOrderDto createOrderDto);

    /**
     * Find page od orders by user ID
     *
     * @param userId   current user ID must not be null
     * @param pageable page number, page size, sort
     * @return page of user's orders
     */
    Page<ResponseOrderDto> findAllByUserId(Long userId, Pageable pageable);
}
