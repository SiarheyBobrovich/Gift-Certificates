package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.data.user.ResponseUserDto;
import ru.clevertec.ecl.entity.User;

@Mapper(uses = OrderMapper.class)
public interface UserMapper {


    ResponseUserDto userToResponseUserDto(User user);

    @Mapping(target = "orders", ignore = true)
    User responseUserDtoToUser(ResponseUserDto responseUserDto);
}
