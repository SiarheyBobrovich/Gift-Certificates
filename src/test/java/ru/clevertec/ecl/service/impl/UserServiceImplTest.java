package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.data.user.ResponseUserDto;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.exception.UserNotFoundException;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.util.TestUserBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static ru.clevertec.ecl.util.TestUserBuilder.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Test
    void checkFindById() {
        User user = builder().build().getUser();
        Optional<User> optionalUser = builder().build().getOptionalUser();
        ResponseUserDto ecpectedDto = builder().build().getResponseUserDto();
        Long id = user.getId();

        doReturn(optionalUser)
                .when(userRepository).findById(id);
        doReturn(ecpectedDto)
                .when(userMapper).userToResponseUserDto(user);

        ResponseUserDto actualDto = userService.findById(id);

        assertThat(actualDto).isEqualTo(ecpectedDto);
    }

    @Test
    void checkFindByIdThrow() {
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
        Map<User, ResponseUserDto> userDtoMap = Map.of(
                userBuilder1.getUser(), userBuilder1.getResponseUserDto(),
                userBuilder2.getUser(), userBuilder2.getResponseUserDto()
        );
        PageImpl<User> repositoryPage = new PageImpl<>(userDtoMap.keySet().stream().toList(), pageable, 2);
        List<ResponseUserDto> expectedContent = userDtoMap.values().stream().toList();

        doReturn(repositoryPage)
                .when(userRepository).findAll(pageable);
        userDtoMap.forEach((user, dto) -> doReturn(dto)
                .when(userMapper).userToResponseUserDto(user));

        List<ResponseUserDto> actualContent = userService.findAll(pageable)
                .getContent();

        assertThat(actualContent)
                .isEqualTo(expectedContent);
    }
}