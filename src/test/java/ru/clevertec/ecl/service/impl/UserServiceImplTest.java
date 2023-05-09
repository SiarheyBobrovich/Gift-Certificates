package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dao.UserRepository;
import ru.clevertec.ecl.data.user.ResponseUserDto;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.exception.UserNotFoundException;
import ru.clevertec.ecl.util.TestUserBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static ru.clevertec.ecl.util.TestUserBuilder.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;


    @Test
    void checkFindById() {
        User user = builder().build().getUser();
        Optional<User> optionalUser = builder().build().getOptionalUser();
        ResponseUserDto responseUserDto = builder().build().getResponseUserDto();
        Long id = user.getId();

        doReturn(optionalUser)
                .when(userRepository).findById(id);

        ResponseUserDto actual = userService.findById(id);

        assertThat(actual).isEqualTo(responseUserDto);
    }

    @Test
    void checkFindByIdThrows() {
        Long id = 1L;
        doReturn(Optional.empty())
                .when(userRepository).findById(id);

        assertThatThrownBy(() -> userService.findById(id))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void checkFindAll() {
        Pageable pageable = Pageable.unpaged();
        TestUserBuilder userBuilder1 = builder().build();
        TestUserBuilder userBuilder2 = builder().id(2L).firstName("second").build();

        List<User> userList = List.of(
                userBuilder1.getUser(), userBuilder2.getUser()
        );
        List<ResponseUserDto> expected = List.of(
                userBuilder1.getResponseUserDto(),
                userBuilder2.getResponseUserDto()
        );

        doReturn(new PageImpl<>(userList, pageable, 2))
                .when(userRepository).findAll(pageable);

        List<ResponseUserDto> actual = userService.findAll(pageable)
                .getContent();

        assertThat(actual)
                .isEqualTo(expected);
    }
}