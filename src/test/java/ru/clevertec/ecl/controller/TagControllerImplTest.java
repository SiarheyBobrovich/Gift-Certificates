package ru.clevertec.ecl.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;
import ru.clevertec.ecl.exception.TagNotFoundException;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.util.TestTagBuilder;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.clevertec.ecl.util.TestTagBuilder.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = TagControllerImpl.class)
class TagControllerImplTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private TagService service;

    private final static String URI = "/api/v1/tags";

    public static Stream<RequestTagDto> getNegateRequestTag() {
        return Stream.of(
                builder().withName(null).build().getRequestTag(),
                builder().withName(" ").build().getRequestTag(),
                builder().withName("\t").build().getRequestTag(),
                builder().withName(",").build().getRequestTag()
        );
    }

    @Nested
    class Get {

        @Test
        void getByIdTag() {
            TestTagBuilder tagBuilder = TestTagBuilder.builder().build();
            ResponseTagDto responseTag = tagBuilder.getResponseTag();
            Long id = responseTag.id();

            doReturn(responseTag)
                    .when(service).findById(id);

            testClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(URI)
                            .pathSegment(String.valueOf(id))
                            .build())
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectHeader()
                    .contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.id").isEqualTo(id)
                    .jsonPath("$.name").isEqualTo(responseTag.name());
        }

        @Test
        void getByIdTagThrows() {
            Long id = 1L;

            doThrow(TagNotFoundException.class)
                    .when(service).findById(id);

            testClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(URI)
                            .pathSegment(String.valueOf(id))
                            .build())
                    .exchange()
                    .expectStatus()
                    .isBadRequest()
                    .expectHeader()
                    .contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.errorMessage").isEmpty()
                    .jsonPath("$.errorCode").isEqualTo(40401);
        }

        @Test
        void checkGetAllTags() {
            Pageable pageable = Pageable.ofSize(20);
            TestTagBuilder tagBuilder1 = builder().build();
            TestTagBuilder tagBuilder2 = builder().withId(2L).build();

            List<ResponseTagDto> tagDtos = List.of(tagBuilder1.getResponseTag(), tagBuilder2.getResponseTag());
            PageImpl<ResponseTagDto> dtoPage = new PageImpl<>(tagDtos, pageable, 2);

            doReturn(dtoPage)
                    .when(service).findAll(pageable);

            testClient.get()
                    .uri(URI)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectHeader()
                    .contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.content").isArray()
                    .jsonPath("$.content").isNotEmpty()
                    .jsonPath("$.totalElements").isEqualTo(2);
        }

        @Test
        void checkGetAllTagsEmpty() {
            Pageable pageable = Pageable.ofSize(20);
            List<ResponseTagDto> tagDtos = List.of();
            PageImpl<ResponseTagDto> dtoPage = new PageImpl<>(tagDtos);

            doReturn(dtoPage)
                    .when(service).findAll(pageable);

            testClient.get()
                    .uri(URI)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectHeader()
                    .contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.content").isArray()
                    .jsonPath("$.content").isEmpty()
                    .jsonPath("$.totalElements").isEqualTo(0);
        }
    }

    @Test
    void checkPostTag() {
        RequestTagDto requestTag = builder().build().getRequestTag();
        ResponseTagDto tagDto = builder().build().getResponseTag();

        doReturn(tagDto)
                .when(service).create(requestTag);

        testClient.post()
                .uri(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestTag)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(ResponseTagDto.class).isEqualTo(tagDto);
    }

    @ParameterizedTest
    @MethodSource("getNegateRequestTag")
    void checkPostTagNegate(RequestTagDto dto) {
        testClient.post()
                .uri(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("$.errorMessage").isNotEmpty()
                .jsonPath("$.errorCode").isEqualTo(400);
    }


    @Test
    void checkPutTag() {
        RequestTagDto requestTag = builder().build().getRequestTag();
        ResponseTagDto tagDto = builder().build().getResponseTag();
        Long id = 1L;

        doReturn(tagDto)
                .when(service).update(id, requestTag);

        testClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path(URI)
                        .pathSegment(String.valueOf(id))
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestTag)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ResponseTagDto.class).isEqualTo(tagDto);
    }

    @Test
    void checkPutTagThrows() {
        RequestTagDto requestTag = builder().build().getRequestTag();

        doThrow(TagNotFoundException.class)
                .when(service).update(1L, requestTag);

        testClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path(URI)
                        .pathSegment("1")
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestTag)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("$.errorMessage").isEmpty()
                .jsonPath("$.errorCode").isEqualTo(40401);
    }


    @ParameterizedTest
    @MethodSource("getNegateRequestTag")
    void checkPutTagNegate(RequestTagDto dto) {
        testClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path(URI)
                        .pathSegment("1")
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("$.errorMessage").isNotEmpty()
                .jsonPath("$.errorCode").isEqualTo(400);

    }

    @Test
    void checkDeleteTag() {
        Long id = 1L;
        testClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path(URI)
                        .pathSegment(String.valueOf(id))
                        .build())
                .exchange()
                .expectStatus()
                .isOk();

        verify(service, times(1))
                .delete(id);
    }

    @Test
    void getMostWidelyTag() {
        ResponseTagDto tagDto = builder().build().getResponseTag();

        doReturn(tagDto)
                .when(service).findMostWidelyTag();

        testClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI)
                        .pathSegment("widely")
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(ResponseTagDto.class).isEqualTo(tagDto);
    }
}
