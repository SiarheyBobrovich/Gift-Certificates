package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.TagRepository;
import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.TagNotFoundException;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.pageable.PageDto;
import ru.clevertec.ecl.service.TagNamesService;
import ru.clevertec.ecl.service.TagService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService, TagNamesService {

    private final TagRepository tagRepository;
    private final TagMapper mapper = Mappers.getMapper(TagMapper.class);

    @Override
    public ResponseTagDto findById(Long id) {
        final Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));
        return mapper.tagToResponseTagDto(tag);
    }

    @Override
    public Page<ResponseTagDto> findAll(Pageable pageable) {
        Page<Tag> tags = tagRepository.findAll(pageable);
        return PageDto.of(tags).map(mapper::tagToResponseTagDto);
    }

    @Override
    @Transactional
    public void create(RequestTagDto dto) {
        final Tag tag = mapper.requestTagDtoToTag(dto);
        tagRepository.save(tag);
    }

    @Override
    @Transactional
    public void update(Long id, RequestTagDto dto) {
        final Tag tag = mapper.requestTagDtoToTag(dto);
        tag.setId(id);
        tagRepository.save(tag);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public List<ResponseTagDto> findByNameIn(Collection<String> names) {
        if (Objects.isNull(names) || names.isEmpty()) {
            return Collections.emptyList();
        }
        List<Tag> tagList = tagRepository.findByNameIn(names);
        return mapper.listTagToListResponseTagDto(tagList);
    }

    @Override
    public ResponseTagDto findMostPopularTag() {
        return tagRepository.findTheMostPopularTag()
                .map(mapper::tagToResponseTagDto)
                .orElseThrow(TagNotFoundException::new);
    }
}
