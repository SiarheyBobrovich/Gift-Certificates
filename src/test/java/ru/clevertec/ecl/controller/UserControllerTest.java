package ru.clevertec.ecl.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.clevertec.ecl.data.user.ResponseUserDto;
import ru.clevertec.ecl.exception.UserNotFoundException;
import ru.clevertec.ecl.service.UserService;
import ru.clevertec.ecl.util.TestUserBuilder;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private UserService service;

    private final static String URI = "/api/v1/users";

    @Test
    void getAllUsers() {
        Pageable pageable = Pageable.ofSize(20);
        TestUserBuilder userBuilder1 = TestUserBuilder.builder().build();
        TestUserBuilder userBuilder2 = TestUserBuilder.builder().id(2L).build();
        List<ResponseUserDto> expectedContent = List.of(userBuilder1.getResponseUserDto(), userBuilder2.getResponseUserDto());
        PageImpl<ResponseUserDto> userDtos = new PageImpl<>(expectedContent);

        doReturn(userDtos)
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
                .jsonPath("$.content").isNotEmpty();
    }

    @Test
    void getAllUsersEmpty() {
        Pageable pageable = Pageable.ofSize(20);
        List<ResponseUserDto> expectedContent = List.of();
        PageImpl<ResponseUserDto> userDtos = new PageImpl<>(expectedContent);

        doReturn(userDtos)
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
                .jsonPath("$.content").isEmpty();
    }

    @Test
    void getById() {
        TestUserBuilder userBuilder1 = TestUserBuilder.builder().build();
        Long id = userBuilder1.getUser().getId();

        doReturn(userBuilder1.getResponseUserDto())
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
                .jsonPath("$.id").isEqualTo(id);
    }

    @Test
    void getByIdThrows() {
        doThrow(UserNotFoundException.class)
                .when(service).findById(any());

        testClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI)
                        .pathSegment("1")
                        .build())
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.errorMessage").isEmpty()
                .jsonPath("$.errorCode").isEqualTo(40003);
    }
}
