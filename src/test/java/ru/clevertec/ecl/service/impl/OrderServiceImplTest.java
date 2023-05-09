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
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.service.UserService;
import ru.clevertec.ecl.util.CertificateBuilder;
import ru.clevertec.ecl.util.TestOrderBuilder;
import ru.clevertec.ecl.util.TestUserBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
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

    @Test
    void findAllByUserId() {
        TestOrderBuilder orderBuilder1 = builder().build();
        TestOrderBuilder orderBuilder2 = builder().purchase(LocalDateTime.MIN).build();
        Pageable pageable = Pageable.unpaged();
        Long userId = 1L;

        PageImpl<Order> orders = new PageImpl<>(List.of(
                orderBuilder1.getOrder(),
                orderBuilder2.getOrder()
        ), pageable, 2);
        List<ResponseOrderDto> expected = List.of(
                orderBuilder1.getResponseOrderDto(),
                orderBuilder2.getResponseOrderDto()
        );

        doReturn(orders)
                .when(repository).findAllByUserId(1L, pageable);

        List<ResponseOrderDto> actual = orderService.findAllByUserId(userId, pageable)
                .getContent();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkCreate() {
        TestOrderBuilder orderBuilder = builder().build();
        Order order = orderBuilder.toBuilder().id(null).purchase(null).build().getOrder();
        CreateOrderDto createOrderDto = orderBuilder.getCreateOrderDto();

        ResponseUserDto responseUserDto = TestUserBuilder.builder().build().getResponseUserDto();
        CertificateBuilder certificateBuilder = CertificateBuilder.builder().build();
        GiftCertificate certificate = certificateBuilder.getEntity();
        ResponseGiftCertificateDto responseCertDto = certificateBuilder.getResponseDto();

        doReturn(responseCertDto)
                .when(gcService).findById(responseCertDto.id());
        doReturn(responseUserDto)
                .when(userService).findById(responseUserDto.id());

        orderService.create(createOrderDto);

        verify(repository, times(1))
                .save(argThat(arg ->
                        Objects.equals(arg.getId(), order.getId()) &&
                                Objects.equals(arg.getPrice(), certificate.getPrice()) &&
                                Objects.equals(arg.getPurchase(), order.getPurchase()) &&
                                Objects.equals(arg.getGiftCertificate(), certificate) &&
                                Objects.equals(arg.getUser().getId(), responseUserDto.id())
                ));
    }
}