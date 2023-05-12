package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.OrderRepository;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.data.order.CreateOrderDto;
import ru.clevertec.ecl.data.order.ResponseOrderDto;
import ru.clevertec.ecl.data.user.ResponseUserDto;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.mapper.AbstractOrderMapper;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.pageable.PageDto;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.service.OrderService;
import ru.clevertec.ecl.service.UserService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final GiftCertificateService gcService;
    private final UserService userService;
    private final OrderRepository repository;
    private final AbstractOrderMapper orderMapper;
    private final UserMapper userMapper;

    @Override
    public Page<ResponseOrderDto> findAllByUserId(Long userId, Pageable pageable) {
        Page<Order> allByUserId = repository.findAllByUserId(userId, pageable);

        return PageDto.of(allByUserId)
                .map(orderMapper::orderToResponseOrderDto);
    }

    @Override
    @Transactional
    public ResponseOrderDto create(CreateOrderDto createOrderDto) {
        ResponseUserDto userDto = userService.findById(createOrderDto.userId());
        ResponseGiftCertificateDto gcDto = gcService.findById(createOrderDto.certificateId());
        User user = userMapper.responseUserDtoToUser(userDto);
        Order order = orderMapper.createOrderDtoToOrder(user, gcDto);
        Order savedOrder = repository.save(order);

        return orderMapper.orderToResponseOrderDto(savedOrder);
    }
}
