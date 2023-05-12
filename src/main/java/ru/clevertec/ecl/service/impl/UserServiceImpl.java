package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.UserRepository;
import ru.clevertec.ecl.data.user.ResponseUserDto;
import ru.clevertec.ecl.exception.UserNotFoundException;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.pageable.PageDto;
import ru.clevertec.ecl.service.UserService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public ResponseUserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::userToResponseUserDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public Page<ResponseUserDto> findAll(Pageable pageable) {
        return PageDto.of(userRepository.findAll(pageable))
                .map(userMapper::userToResponseUserDto);
    }
}
