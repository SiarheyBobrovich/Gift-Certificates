package ru.clevertec.ecl.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.pageable.Filter;
import ru.clevertec.ecl.pageable.PageDto;
import ru.clevertec.ecl.pageable.Patch;
import ru.clevertec.ecl.service.GiftCertificateService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static ru.clevertec.ecl.util.CertificateBuilder.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = GiftCertificateControllerImpl.class)
class GiftCertificateControllerImplTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private GiftCertificateService service;

    private final static String URI = "/api/v1/certificates";

    @Nested
    class Get {

        @Test
        void checkGetById_1() {
            ResponseGiftCertificateDto responseGiftCertificateDto = builder().id(1L).build().getResponseDto();
            doReturn(responseGiftCertificateDto)
                    .when(service).findById(1L);

            testClient.get()
                    .uri(URI + "/1")
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectHeader()
                    .contentType(MediaType.APPLICATION_JSON)
                    .expectBody(ResponseGiftCertificateDto.class)
                    .isEqualTo(responseGiftCertificateDto);
        }

        @Test
        void checkGetAllGiftCertificates() {
            Page<Object> empty = Page.empty();
            doReturn(empty)
                    .when(service).findAll(Pageable.ofSize(20));

            testClient.get()
                    .uri(URI)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectHeader()
                    .contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.content").isArray()
                    .jsonPath("$.content").isEmpty();
        }

        @Test
        void checkGetByThrows() {
            doThrow(GiftCertificateNotFoundException.class)
                    .when(service).findById(1L);
            testClient.get()
                    .uri(URI + "/1")
                    .exchange()
                    .expectStatus()
                    .isBadRequest()
                    .expectBody()
                    .json("{\"errorMessage\":null,\"errorCode\":404002}");
        }

        @Test
        void checkGetByFilter() {
            ResponseGiftCertificateDto responseGiftCertificateDto = builder().build().getResponseDto();
            Filter filter = Filter.builder().tag("tttt").part("cer").build();
            Pageable pageable = Pageable.ofSize(10);
            PageDto<ResponseGiftCertificateDto> page = PageDto.of(new PageImpl<>(List.of(responseGiftCertificateDto), pageable, 50L));

            MultiValueMap<String, String> part = new LinkedMultiValueMap<>();
            part.put("part", List.of(filter.getPart()));
            part.put("tag", List.of(filter.getTag()));
            part.put("size", List.of("10"));

            doReturn(page)
                    .when(service).findByFilter(any(), any());

            testClient.get()
                    .uri(uriBuilder ->
                            uriBuilder.path(URI)
                                    .pathSegment("filter")
                                    .queryParams(part)
                                    .build())
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectHeader()
                    .contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.size").isEqualTo(page.getSize())
                    .jsonPath("$.number").isEqualTo(page.getNumber())
                    .jsonPath("$.totalPages").isEqualTo(page.getTotalPages())
                    .jsonPath("$.totalElements").isEqualTo(page.getTotalElements())
                    .jsonPath("$.content").isArray()
                    .jsonPath("$.content").isNotEmpty();
        }

        @ParameterizedTest
        @MethodSource("getFilterNegate")
        void checkGetByFilterNegate(Filter filter) {
            ArrayList<String> tag = new ArrayList<>();
            tag.add(filter.getTag());
            ArrayList<String> part = new ArrayList<>();
            part.add(filter.getPart());
            MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
            param.put("part", part);
            param.put("tag", tag);

            testClient.get()
                    .uri(uriBuilder ->
                            uriBuilder.path(URI)
                                    .path("/filter")
                                    .queryParams(param)
                                    .build())
                    .exchange()
                    .expectStatus()
                    .isBadRequest();
        }

        private static Stream<Filter> getFilterNegate() {
            return Stream.of(
                    Filter.builder().tag("").build(),
                    Filter.builder().part("").build()
            );
        }
    }

    @Test
    void checkPostGiftCertificate() {
        RequestGiftCertificateDto dto = builder().build().getRequestDto();
        ResponseGiftCertificateDto certificateDto = builder().build().getResponseDto();

        doReturn(certificateDto)
                .when(service).create(dto);

        testClient.post()
                .uri(URI)
                .body(Mono.just(dto), ResponseGiftCertificateDto.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(ResponseGiftCertificateDto.class).isEqualTo(certificateDto);
    }

    @ParameterizedTest
    @MethodSource("certificateDtoStream")
    void checkPostGiftCertificateNegate(RequestGiftCertificateDto dto) {
        testClient.post()
                .uri(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(dto), RequestGiftCertificateDto.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @ParameterizedTest
    @MethodSource("certificateDtoStream")
    void checkPutGiftCertificateNegate(RequestGiftCertificateDto dto) {
        testClient.put()
                .uri(URI + "/" + 1L)
                .body(Mono.just(dto), RequestGiftCertificateDto.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void checkPutGiftCertificate() {
        RequestGiftCertificateDto dto = builder().build().getRequestDto();
        ResponseGiftCertificateDto certificate = builder().build().getResponseDto();
        Long id = 1L;

        doReturn(certificate).when(service).update(id, dto);

        testClient.put()
                .uri(URI + "/" + id)
                .body(Mono.just(dto), ResponseGiftCertificateDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(ResponseGiftCertificateDto.class).isEqualTo(certificate);
    }

    @Test
    void checkPutGiftCertificateThrow() {
        RequestGiftCertificateDto dto = builder().build().getRequestDto();
        Long id = 1L;

        doThrow(GiftCertificateNotFoundException.class).when(service).update(id, dto);

        testClient.put()
                .uri(URI + "/" + id)
                .body(Mono.just(dto), ResponseGiftCertificateDto.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void checkDeleteGiftCertificate() {
        testClient.delete()
                .uri(URI + "/1")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @ParameterizedTest
    @MethodSource("getPatch")
    void checkPatchGiftCertificate(Patch patch) {
        ResponseGiftCertificateDto certificateDto;
        Long id = 1L;

        if (Objects.equals(patch.field(), "description")) {
            certificateDto = builder().description(patch.value()).build().getResponseDto();
        } else {
            certificateDto = builder().price(new BigDecimal(patch.value())).build().getResponseDto();
        }

        doReturn(certificateDto)
                .when(service).patch(id, patch);

        testClient.patch()
                .uri(URI + "/" + id)
                .body(Mono.just(patch), Patch.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(ResponseGiftCertificateDto.class).isEqualTo(certificateDto);
    }

    @ParameterizedTest
    @MethodSource("getPatchNegate")
    void checkPatchGiftCertificateNegate(Patch patch) {
        testClient.patch()
                .uri(URI + "/" + 1)
                .body(Mono.just(patch), Patch.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    static Stream<Patch> getPatch() {
        return Stream.of(
                new Patch("price", "1.13"),
                new Patch("description", "\"description\"")
        );
    }

    static Stream<Patch> getPatchNegate() {
        return Stream.of(
                new Patch("price", "1,13"),
                new Patch(null, null),
                new Patch(null, "\"null\""),
                new Patch("null", null),
                new Patch("description", "description")
        );
    }

    static Stream<RequestGiftCertificateDto> certificateDtoStream() {
        return Stream.of(
                builder().name(null).build().getRequestDto(),
                builder().name("null").build().getRequestDto(),                              //4
                builder().name("adfgtiokdntajstptnghsiknglpungq").build().getRequestDto(),  //31
                builder().price(null).build().getRequestDto(),
                builder().price(BigDecimal.valueOf(-1)).build().getRequestDto(),
                builder().description(null).build().getRequestDto(),
                builder().description("").build().getRequestDto(),
                builder().description(" ").build().getRequestDto(),
                builder().duration(null).build().getRequestDto(),
                builder().duration(0).build().getRequestDto(),
                builder().duration(-1).build().getRequestDto()
        );
    }
}
