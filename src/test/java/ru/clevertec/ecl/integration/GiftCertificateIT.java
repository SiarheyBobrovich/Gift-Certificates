package ru.clevertec.ecl.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.pageable.PageDto;
import ru.clevertec.ecl.pageable.Patch;
import ru.clevertec.ecl.util.CertificateBuilder;
import ru.clevertec.ecl.util.IntegrationTest;
import ru.clevertec.ecl.util.PostgresTestContainer;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.clevertec.ecl.util.CertificateBuilder.builder;
import static ru.clevertec.ecl.util.CertificateBuilder.getResponseList;

@IntegrationTest
@Sql("classpath:sql_script/certificate_IT.sql")
public class GiftCertificateIT extends PostgresTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private static final String URI = "/api/v1/certificates";

    @Test
    @SneakyThrows
    void checkGetById() {
        ResponseGiftCertificateDto expectedDto = getResponseList().get(0);
        Long id = expectedDto.id();
        String expectedJson = mapper.writeValueAsString(expectedDto);

        mockMvc.perform(get(URI + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @SneakyThrows
    void checkGetAll() {
        List<ResponseGiftCertificateDto> expectedDto = getResponseList();
        Pageable pageable = Pageable.ofSize(20);
        Page<ResponseGiftCertificateDto> expectedPage = new PageDto<>(expectedDto, pageable, 2L);

        String expectedJson = mapper.writeValueAsString(expectedPage);

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @SneakyThrows
    void checkPost() {
        CertificateBuilder cBuilder = builder().id(3L)
                .name("Certificate")
                .description("description3")
                .price(BigDecimal.valueOf(3))
                .duration(3)
                .tags(List.of(new Tag(4L, "Fourth")))
                .build();
        RequestGiftCertificateDto requestDto = cBuilder.getRequestDto();
        ResponseGiftCertificateDto expectedDto = cBuilder.getResponseDto();
        String content = mapper.writeValueAsString(requestDto);

        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(expectedDto.id()))
                .andExpect(jsonPath("name").value(expectedDto.name()))
                .andExpect(jsonPath("description").value(expectedDto.description()))
                .andExpect(jsonPath("price").value(expectedDto.price()))
                .andExpect(jsonPath("duration").value(expectedDto.duration()))
                .andExpect(jsonPath("tags").isArray())
                .andExpect(jsonPath("tags").isNotEmpty());
    }

    @Test
    @SneakyThrows
    void checkPut() {
        CertificateBuilder cBuilder = builder()
                .name("Certificate")
                .description("description")
                .price(BigDecimal.valueOf(3))
                .duration(3)
                .tags(List.of(new Tag(4L, "Fourth")))
                .build();
        RequestGiftCertificateDto requestDto = cBuilder.getRequestDto();
        ResponseGiftCertificateDto expectedDto = cBuilder.getResponseDto();
        Long id = expectedDto.id();
        String content = mapper.writeValueAsString(requestDto);

        mockMvc.perform(put(URI + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("id").value(expectedDto.id()),
                        jsonPath("name").value(expectedDto.name()),
                        jsonPath("description").value(expectedDto.description()),
                        jsonPath("price").value(expectedDto.price()),
                        jsonPath("duration").value(expectedDto.duration()));
    }

    @Test
    @SneakyThrows
    void checkDelete() {
        Long id = getResponseList().get(0).id();
        mockMvc.perform(delete(URI + "/{id}", id))
                .andExpectAll(
                        status().isOk()
                );
    }

    @Test
    @SneakyThrows
    void checkGetByFilter() {
        Pageable pageable = Pageable.ofSize(20);
        PageDto<ResponseGiftCertificateDto> expected = new PageDto<>(List.of(getResponseList().get(1)), pageable, 1L);
        String expectedJsonPage = mapper.writeValueAsString(expected);

        mockMvc.perform(get(URI + "/filter")
                        .param("part", "2")
                        .param("tag", "Two"))
                .andExpectAll(
                        status().isOk(),
                        content().json(expectedJsonPage)
                );
    }

    @Test
    @SneakyThrows
    void checkPatchGiftCertificate() {
        Patch patch = new Patch("price", "100500");

        mockMvc.perform(patch(URI + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(patch)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.price").value(BigDecimal.valueOf(100500))
                );
    }
}
