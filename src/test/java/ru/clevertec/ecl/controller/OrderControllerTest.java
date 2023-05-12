package ru.clevertec.ecl.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.clevertec.ecl.data.order.CreateOrderDto;
import ru.clevertec.ecl.data.order.ResponseOrderDto;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.pageable.PageDto;
import ru.clevertec.ecl.service.OrderService;
import ru.clevertec.ecl.util.TestOrderBuilder;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.doReturn;
import static ru.clevertec.ecl.util.TestOrderBuilder.builder;

@ActiveProfiles("test")
@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private OrderService service;

    private final static String URI = "/api/v1/orders";

    public static Stream<CreateOrderDto> getOrdersNegate() {
        return Stream.of(
                builder().giftCertificate(new GiftCertificate()).build().getCreateOrderDto(),
                builder().user(new User()).build().getCreateOrderDto()
        );
    }

    @Test
    void findAllOrdersByUserId() {
        Pageable pageable = Pageable.ofSize(20);
        ResponseOrderDto orderDto1 = builder().build().getResponseOrderDto();
        ResponseOrderDto orderDto2 = builder().id(2L).build().getResponseOrderDto();
        Long userId = orderDto1.id();
        PageDto<ResponseOrderDto> expectedPage = new PageDto<>(List.of(orderDto1, orderDto2), pageable, 2);

        doReturn(expectedPage)
                .when(service).findAllByUserId(userId, pageable);

        testClient.get()
                .uri(uriBuilder -> uriBuilder.path(URI)
                        .pathSegment(String.valueOf(userId))
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("number").isEqualTo(0)
                .jsonPath("size").isEqualTo(20)
                .jsonPath("content.*").isNotEmpty()
                .jsonPath("content").isArray();
    }

    @Test
    void createNewOrder() {
        TestOrderBuilder orderBuilder = builder().build();
        ResponseOrderDto expectedDto = orderBuilder.getResponseOrderDto();
        CreateOrderDto createOrderDto = orderBuilder.getCreateOrderDto();

        doReturn(expectedDto)
                .when(service).create(createOrderDto);

        testClient.post()
                .uri(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createOrderDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ResponseOrderDto.class)
                .isEqualTo(expectedDto);
    }

    @ParameterizedTest
    @MethodSource("getOrdersNegate")
    void createNewOrderNegate(CreateOrderDto dto) {
        testClient.post()
                .uri(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
