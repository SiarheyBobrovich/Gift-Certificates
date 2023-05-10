package ru.clevertec.ecl.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.clevertec.ecl.service.OrderService;

@WebMvcTest(controllers = OrderController.class)
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private OrderService service;

    private final static String URI = "/api/v1/orders";

    @Test
    void findAllOrdersByUserId() {
    }

    @Test
    void createNewOrder() {
    }
}