package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;
import ru.clevertec.ecl.entity.Tag;

import java.util.List;

@Mapper
public interface TagMapper {

    ResponseTagDto tagToResponseTagDto(Tag tag);

    @Mapping(target = "id", ignore = true)
    Tag requestTagDtoToTag(RequestTagDto dto);

    List<ResponseTagDto> listTagToListResponseTagDto(List<Tag> tags);
}
