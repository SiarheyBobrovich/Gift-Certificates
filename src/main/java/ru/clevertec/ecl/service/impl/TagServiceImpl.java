package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dao.TagRepository;
import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.TagNotFoundException;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.service.TagService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper mapper = Mappers.getMapper(TagMapper.class);

    @Override
    public ResponseTagDto findById(Long id) {
        final Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));
        return mapper.tagToResponseTagDto(tag);
    }

    @Override
    public List<ResponseTagDto> findAll() {
        List<Tag> tags = tagRepository.findAll();
        return mapper.listTagToListResponseTagDto(tags);
    }

    @Override
    public void create(RequestTagDto dto) {
        final Tag tag = mapper.requestTagDtoToTag(dto);
        tagRepository.save(tag);
    }

    @Override
    public void update(Long id, RequestTagDto dto) {
        final Tag tag = mapper.requestTagDtoToTag(dto);
        tag.setId(id);
        tagRepository.update(tag);
    }

    @Override
    public void delete(Long id) {
        tagRepository.delete(id);
    }
}
