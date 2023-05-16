package ru.clevertec.ecl.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.controller.open_api.OrderOpenApi;
import ru.clevertec.ecl.data.order.CreateOrderDto;
import ru.clevertec.ecl.data.order.ResponseOrderDto;
import ru.clevertec.ecl.service.OrderService;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController implements OrderOpenApi {

    private final OrderService orderService;

    @Override
    @GetMapping("/{userId}")
    public ResponseEntity<Page<ResponseOrderDto>> findAllOrdersByUserId(@PageableDefault(20) Pageable pageable,
                                                                        @PathVariable Long userId) {
        Page<ResponseOrderDto> responseOrderDtoPage = orderService.findAllByUserId(userId, pageable);
        log.info("GET/{}\nResponse::{}", userId, responseOrderDtoPage);

        return ResponseEntity.ok(responseOrderDtoPage);
    }

    @Override
    @PostMapping
    public ResponseEntity<ResponseOrderDto> createNewOrder(@RequestBody @Valid CreateOrderDto createOrderDto) {
        ResponseOrderDto orderDto = orderService.create(createOrderDto);
        log.info("POST::{}\nResponse::{}", createOrderDto, orderDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderDto);
    }
}
