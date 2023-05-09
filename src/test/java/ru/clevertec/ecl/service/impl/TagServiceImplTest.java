package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dao.TagRepository;
import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.exception.TagNotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
        Optional<Tag> optionalTag = Optional.of(Tag.builder()
                .id(id)
                .name("name")
                .build());
        doReturn(optionalTag).when(tagRepository).findById(1L);

        ResponseTagDto tagDto = tagService.findById(id);
        Long expectedId = tagDto.id();
        String name = tagDto.name();

        assertThat(expectedId).isEqualTo(id);
        assertThat(name).isEqualTo("name");
    }

    @Test
    void checkFindByThrows() {
        doReturn(Optional.empty()).when(tagRepository).findById(1L);

        assertThatThrownBy(() -> tagService.findById(1L)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void checkFindAll() {
        Pageable pageable = Pageable.unpaged();
        PageImpl<Tag> tagPage = new PageImpl<>(List.of(
                Tag.builder().id(1L).name("name1").build(),
                Tag.builder().id(2L).name("name2").build()), pageable, 2);

        doReturn(tagPage).when(tagRepository).findAll(pageable);

        Page<ResponseTagDto> responseTagDtoPage = tagService.findAll(pageable);
        List<ResponseTagDto> actual = responseTagDtoPage.getContent();

        IntStream.range(0, actual.size())
                .forEach(x -> {
                    Long actualId = actual.get(x).id();
                    Long expectedId = tagPage.getContent().get(x).getId();

                    String actualName = actual.get(x).name();
                    String expectedName = actual.get(x).name();

                    assertThat(actualId).isEqualTo(expectedId);
                    assertThat(actualName).isEqualTo(expectedName);
                });
    }

    @Test
    void checkFindAllEmpty() {
        Pageable pageable = Pageable.unpaged();
        doReturn(Page.empty()).when(tagRepository).findAll(pageable);

        Page<ResponseTagDto> all = tagService.findAll(pageable);

        assertThat(all).isEmpty();
    }

    @Test
    void checkFindByNameInEmpty() {
        List<ResponseTagDto> byNameIn = tagService.findByNameIn(List.of());
        assertThat(byNameIn).isEmpty();
    }

    @Test
    void checkFindByNameInNull() {
        List<ResponseTagDto> byNameIn = tagService.findByNameIn(null);
        assertThat(byNameIn).isEmpty();
    }


    @Test
    void checkFindByNameIn() {
        List<Tag> tags = Stream.of(
                Tag.builder().id(1L).name("tag1").build(),
                Tag.builder().id(2L).name("tag2").build(),
                Tag.builder().id(3L).name("tag3").build()
        ).collect(Collectors.toList());
        List<String> names = tags.stream().map(Tag::getName)
                .collect(Collectors.toList());

        doReturn(tags)
                .when(tagRepository).findByNameIn(names);

        List<ResponseTagDto> actual = tagService.findByNameIn(names);

        assertThat(actual.stream()
                .map(ResponseTagDto::name)
                .allMatch(names::contains))
                .isTrue();
    }

    @Nested
    class Create {

        @Test
        void checkCreate() {
            RequestTagDto tagDto = new RequestTagDto("name");

            doReturn(Optional.empty())
                    .when(tagRepository).findByName("name");

            tagService.create(tagDto);

            verify(tagRepository, times(1))
                    .save(argThat(arg -> Objects.isNull(arg.getId()) &&
                            Objects.equals("name", arg.getName())));
        }

        @Test
        void checkCreateDoesNotSave() {
            RequestTagDto tagDto = new RequestTagDto("name");
            Tag tag = Tag.builder().name("name").build();

            doReturn(Optional.of(tag))
                    .when(tagRepository).findByName("name");

            tagService.create(tagDto);

            verify(tagRepository, times(0))
                    .save(any());
        }
    }

    @Nested
    class Update {

        @Test
        void checkUpdate() {
            RequestTagDto tagDto = new RequestTagDto("name");
            Tag tagUpdate = Tag.builder().id(1L).name("name").build();

            doReturn(tagUpdate).when(tagRepository).save(tagUpdate);

            tagService.update(1L, tagDto);

            verify(tagRepository, times(1)).save(tagUpdate);
        }
    }

    @Test
    void checkDelete() {
        tagService.delete(1L);

        verify(tagRepository, times(1))
                .deleteById(1L);
    }

    @Test
    void checkFindMostPopularTag() {
        Tag tag = Tag.builder().id(1L).name("popular").build();

        doReturn(Optional.of(tag))
                .when(tagRepository).findTheMostPopularTag();

        ResponseTagDto mostPopularTag = tagService.findMostPopularTag();

        assertThat(
                Objects.equals(mostPopularTag.id(), tag.getId()) &&
                        Objects.equals(mostPopularTag.name(), tag.getName()))
                .isTrue();
    }

    @Test
    void checkFindMostPopularTagThrows() {
        doReturn(Optional.empty())
                .when(tagRepository).findTheMostPopularTag();

        assertThatThrownBy(() -> tagService.findMostPopularTag())
                .isInstanceOf(TagNotFoundException.class);
    }
}