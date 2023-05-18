package ru.clevertec.ecl.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.ecl.data.order.ResponseOrderDto;
import ru.clevertec.ecl.data.user.ResponseUserDto;
import ru.clevertec.ecl.util.IntegrationTest;
import ru.clevertec.ecl.util.PostgresTestContainer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@Sql("classpath:sql_script/order_IT.sql")
class UserIT extends PostgresTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private static final String URI = "/api/v1/users";

    @Test
    @SneakyThrows
    void getAllUsers() {
        mockMvc.perform(get(URI))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("number").value(0L),
                        jsonPath("size").value(20L),
                        jsonPath("totalElements").value(2),
                        jsonPath("content").isArray(),
                        jsonPath("content").isNotEmpty()
                );
    }

    @Test
    @SneakyThrows
    void getById() {
        List<ResponseOrderDto> orders = List.of(
                new ResponseOrderDto(5L, 2L, BigDecimal.TEN, LocalDateTime.parse("2023-01-01T00:00:00")),
                new ResponseOrderDto(6L, 1L, BigDecimal.ONE, LocalDateTime.parse("2023-01-01T00:00:00"))
        );
        ResponseUserDto expectedUser = new ResponseUserDto(2L, "Two", "Second", orders);
        Long id = expectedUser.id();

        mockMvc.perform(get(URI + "/{id}", id))
                .andExpectAll(
                        status().isOk(),
                        content().json(mapper.writeValueAsString(expectedUser))
                );
    }
}
