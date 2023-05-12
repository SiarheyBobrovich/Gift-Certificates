package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dao.OrderRepository;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.data.order.CreateOrderDto;
import ru.clevertec.ecl.data.order.ResponseOrderDto;
import ru.clevertec.ecl.data.user.ResponseUserDto;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.mapper.AbstractOrderMapper;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.service.UserService;
import ru.clevertec.ecl.util.CertificateBuilder;
import ru.clevertec.ecl.util.TestOrderBuilder;
import ru.clevertec.ecl.util.TestUserBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static ru.clevertec.ecl.util.TestOrderBuilder.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private GiftCertificateService gcService;

    @Mock
    private UserService userService;

    @Mock
    private OrderRepository repository;

    @Mock
    private AbstractOrderMapper orderMapper;

    @Mock
    private UserMapper userMapper;

    @Test
    void findAllByUserId() {
        Long userId = 1L;
        TestOrderBuilder orderBuilder1 = builder().build();
        TestOrderBuilder orderBuilder2 = builder().id(2L).build();
        Order order1 = orderBuilder1.getOrder();
        Order order2 = orderBuilder2.getOrder();
        ResponseOrderDto responseOrderDto1 = orderBuilder1.getResponseOrderDto();
        ResponseOrderDto responseOrderDto2 = orderBuilder2.getResponseOrderDto();
        Pageable pageable = Pageable.unpaged();

        PageImpl<Order> repositoryPage = new PageImpl<>(
                List.of(order1, order2), pageable, 2);
        List<ResponseOrderDto> expected = List.of(responseOrderDto1, responseOrderDto2);

        doReturn(repositoryPage)
                .when(repository).findAllByUserId(1L, pageable);
        doReturn(responseOrderDto1)
                .when(orderMapper).orderToResponseOrderDto(order1);
        doReturn(responseOrderDto2)
                .when(orderMapper).orderToResponseOrderDto(order2);

        List<ResponseOrderDto> actual = orderService.findAllByUserId(userId, pageable)
                .getContent();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkCreate() {
        TestOrderBuilder orderBuilder = builder().build();
        Order savedOrder = orderBuilder.getOrder();
        ResponseOrderDto expectedDto = orderBuilder.getResponseOrderDto();
        Order newOrder = orderBuilder.toBuilder()
                .id(null)
                .purchase(null)
                .build()
                .getOrder();
        CreateOrderDto createOrderDto = orderBuilder.getCreateOrderDto();

        ResponseUserDto responseUserDto = TestUserBuilder.builder().build().getResponseUserDto();
        CertificateBuilder certificateBuilder = CertificateBuilder.builder().build();
        ResponseGiftCertificateDto responseCertDto = certificateBuilder.getResponseDto();

        doReturn(responseUserDto)
                .when(userService).findById(responseUserDto.id());
        doReturn(newOrder.getUser())
                .when(userMapper).responseUserDtoToUser(responseUserDto);
        doReturn(responseCertDto)
                .when(gcService).findById(responseCertDto.id());
        doReturn(newOrder)
                .when(orderMapper).createOrderDtoToOrder(newOrder.getUser(), responseCertDto);
        doReturn(savedOrder)
                .when(repository).save(newOrder);
        doReturn(expectedDto)
                .when(orderMapper).orderToResponseOrderDto(savedOrder);

        ResponseOrderDto actualDto = orderService.create(createOrderDto);

        assertThat(actualDto).isEqualTo(expectedDto);
    }
}
