package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
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

    private final UserRepository repository;
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Override
    public ResponseUserDto findById(Long id) {
        return repository.findById(id)
                .map(mapper::userToResponseUserDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public Page<ResponseUserDto> findAll(Pageable pageable) {
        return PageDto.of(repository.findAll(pageable))
                .map(mapper::userToResponseUserDto);
    }
}
