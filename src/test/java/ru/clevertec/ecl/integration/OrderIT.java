package ru.clevertec.ecl.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.ecl.data.order.CreateOrderDto;
import ru.clevertec.ecl.data.order.ResponseOrderDto;
import ru.clevertec.ecl.pageable.PageDto;
import ru.clevertec.ecl.util.IntegrationTest;
import ru.clevertec.ecl.util.PostgresTestContainer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@Sql("classpath:sql_script/order_IT.sql")
class OrderIT extends PostgresTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private static final String URI = "/api/v1/orders";

    @Test
    @SneakyThrows
    void findAllOrdersByUserId() {
        long id = 2L;
        Pageable pageable = Pageable.ofSize(20);
        List<ResponseOrderDto> content = List.of(
                new ResponseOrderDto(5L, 2L, BigDecimal.TEN, LocalDateTime.parse("2023-01-01T00:00:00")),
                new ResponseOrderDto(6L, 1L, BigDecimal.ONE, LocalDateTime.parse("2023-01-01T00:00:00"))
        );
        PageDto<ResponseOrderDto> expectedPage = new PageDto<>(content, pageable, 2L);

        mockMvc.perform(get(URI + "/{id}", id))
                .andExpectAll(
                        status().isOk(),
                        content().json(mapper.writeValueAsString(expectedPage))
                );
    }

    @Test
    @SneakyThrows
    void createNewOrder() {
        CreateOrderDto orderDto = new CreateOrderDto(2L, 1L);
        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(orderDto)))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("id").value(7L),
                        jsonPath("giftCertificateId").value(1L),
                        jsonPath("price").value(BigDecimal.valueOf(1.11)),
                        jsonPath("purchase").exists(),
                        jsonPath("purchase").isNotEmpty()
                );

    }
}
