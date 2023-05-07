package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.data.order.CreateOrderDto;
import ru.clevertec.ecl.data.order.ResponseOrderDto;
import ru.clevertec.ecl.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{userId}")
    public ResponseEntity<Page<ResponseOrderDto>> findAllOrdersByUserId(@PathVariable Long userId,
                                                                        @PageableDefault(20) Pageable pageable) {

        Page<ResponseOrderDto> responseOrderDtoPage = orderService.findAllByUserId(userId, pageable);
        return ResponseEntity.ok().body(responseOrderDtoPage);
    }

    @PostMapping
    public ResponseEntity<Void> createNewOrder(@RequestBody CreateOrderDto createOrderDto) {
        orderService.create(createOrderDto);
        return ResponseEntity.status(204).build();
    }
}
