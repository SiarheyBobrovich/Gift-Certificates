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

import static org.mockito.Mockito.*;
import static ru.clevertec.ecl.util.TestTagBuilder.*;

@WebMvcTest(controllers = TagControllerImpl.class)
@ActiveProfiles("test")
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
        void getAllTags() {
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
        void getAllTagsEmpty() {
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
    void postTag() {
        RequestTagDto requestTag = builder().build().getRequestTag();

        testClient.post()
                .uri(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestTag)
                .exchange()
                .expectStatus()
                .isCreated();

        verify(service, times(1)).create(requestTag);
    }

    @ParameterizedTest
    @MethodSource("getNegateRequestTag")
    void postTagNegate(RequestTagDto dto) {
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
    void putTag() {
        RequestTagDto requestTag = builder().build().getRequestTag();
        Long id = 1L;

        testClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path(URI)
                        .pathSegment(String.valueOf(id))
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestTag)
                .exchange()
                .expectStatus()
                .isCreated();

        verify(service, times(1)).update(id, requestTag);
    }

    @Test
    void putTagThrows() {
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
    void putTagNegate(RequestTagDto dto) {
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
    void deleteTag() {
        Long id = 1L;
        testClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path(URI)
                        .pathSegment(String.valueOf(id))
                        .build())
                .exchange()
                .expectStatus()
                .isNoContent();

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
                        .pathSegment("popular")
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(ResponseTagDto.class).isEqualTo(tagDto);
    }
}
