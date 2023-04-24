package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.dao.TagRepository;
import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @Test
    void checkFindById() {
        Long id = 1L;
        Optional<Tag> optionalTag = Optional.of(Tag.builder().id(id).name("name").build());
        doReturn(optionalTag).when(tagRepository).findById(1L);

        ResponseTagDto tagDto = tagService.findById(id);
        Long expectedId = tagDto.id();
        String name = tagDto.name();

        assertThat(expectedId).isEqualTo(id);
        assertThat(name).isEqualTo("name");
    }

    @Test
    void checkFindByIdEntityNotFoundException() {
        doReturn(Optional.empty()).when(tagRepository).findById(1L);

        assertThatThrownBy(() -> tagService.findById(1L)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void checkFindAll() {
        List<Tag> tags = List.of(
                Tag.builder().id(1L).name("name1").build(),
                Tag.builder().id(2L).name("name2").build());

        doReturn(tags).when(tagRepository).findAll();

        List<ResponseTagDto> all = tagService.findAll();

        IntStream.range(0, all.size())
                .forEach(x -> {
                    Long actualId = all.get(x).id();
                    Long expectedId = tags.get(x).getId();

                    String actualName = all.get(x).name();
                    String expectedName = all.get(x).name();

                    assertThat(actualId).isEqualTo(expectedId);
                    assertThat(actualName).isEqualTo(expectedName);
                });
    }

    @Test
    void checkFindAllEmpty() {
        doReturn(List.of()).when(tagRepository).findAll();

        List<ResponseTagDto> all = tagService.findAll();

        assertThat(all).isEmpty();
    }

    @Test
    void checkCreate() {
        RequestTagDto tagDto = new RequestTagDto("name");
        Tag tagSave = Tag.builder().name("name").build();
        Tag tag = Tag.builder().id(1L).name("name").build();

        doReturn(tag).when(tagRepository).save(tagSave);

        tagService.create(tagDto);

        verify(tagRepository, times(1)).save(tagSave);
    }

    @Test
    void checkUpdate() {
        RequestTagDto tagDto = new RequestTagDto("name");
        Tag tagUpdate = Tag.builder().id(1L).name("name").build();

        doReturn(tagUpdate).when(tagRepository).update(tagUpdate);

        tagService.update(1L, tagDto);

        verify(tagRepository, times(1)).update(tagUpdate);
    }
}