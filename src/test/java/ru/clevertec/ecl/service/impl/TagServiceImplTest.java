package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.exception.TagExistsException;
import ru.clevertec.ecl.exception.TagNotFoundException;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.util.TestTagBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.clevertec.ecl.util.TestTagBuilder.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagMapper tagMapper;

    @Test
    void checkFindById() {
        TestTagBuilder tagBuilder = builder().build();
        Optional<Tag> optionalTag = tagBuilder.getOptional();
        Tag tag = tagBuilder.getTag();
        Long id = tag.getId();
        ResponseTagDto expectedDto = tagBuilder.getResponseTag();

        doReturn(optionalTag)
                .when(tagRepository).findById(id);
        doReturn(expectedDto)
                .when(tagMapper).tagToResponseTagDto(tag);

        ResponseTagDto actualDto = tagService.findById(id);

        assertThat(actualDto).isEqualTo(expectedDto);
    }

    @Test
    void checkFindByThrows() {
        doReturn(Optional.empty()).when(tagRepository).findById(1L);

        assertThatThrownBy(() -> tagService.findById(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void checkFindAll() {
        TestTagBuilder tagBuilder1 = builder().build();
        TestTagBuilder tagBuilder2 = builder().withId(2L).withName("tag2").build();
        Map<Tag, ResponseTagDto> tagResponseTagDtoMap = Map.of(
                tagBuilder1.getTag(), tagBuilder1.getResponseTag(),
                tagBuilder2.getTag(), tagBuilder2.getResponseTag());
        Pageable pageable = Pageable.unpaged();
        PageImpl<Tag> repositoryPage = new PageImpl<>(tagResponseTagDtoMap.keySet().stream().toList());
        List<ResponseTagDto> expectedContent = tagResponseTagDtoMap.values().stream().toList();

        doReturn(repositoryPage)
                .when(tagRepository).findAll(pageable);
        tagResponseTagDtoMap.forEach((tag, tagDto) -> doReturn(tagDto)
                .when(tagMapper).tagToResponseTagDto(tag));

        Page<ResponseTagDto> responseTagDtoPage = tagService.findAll(pageable);
        List<ResponseTagDto> actualContent = responseTagDtoPage.getContent();

        assertThat(actualContent).isEqualTo(expectedContent);
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
        TestTagBuilder tagBuilder1 = builder().build();
        TestTagBuilder tagBuilder2 = builder().withId(1L).build();
        TestTagBuilder tagBuilder3 = builder().withId(2L).build();
        List<Tag> tags = Stream.of(
                tagBuilder1.getTag(),
                tagBuilder2.getTag(),
                tagBuilder3.getTag()
        ).collect(Collectors.toList());
        List<ResponseTagDto> expectedDtoList = Stream.of(
                tagBuilder1.getResponseTag(),
                tagBuilder2.getResponseTag(),
                tagBuilder3.getResponseTag()
        ).collect(Collectors.toList());
        List<String> names = tags.stream()
                .map(Tag::getName)
                .collect(Collectors.toList());

        doReturn(tags)
                .when(tagRepository).findByNameIn(names);
        doReturn(expectedDtoList)
                .when(tagMapper).listTagToListResponseTagDto(tags);

        List<ResponseTagDto> actual = tagService.findByNameIn(names);

        assertThat(actual).isEqualTo(expectedDtoList);
    }

    @Nested
    class Create {

        @Test
        void checkCreate() {
            TestTagBuilder tagBuilder = builder().build();
            RequestTagDto requestTag = tagBuilder.getRequestTag();
            ResponseTagDto responseTag = tagBuilder.getResponseTag();
            Tag newTag = tagBuilder.getTagWithoutId();
            Tag savedTag = tagBuilder.getTag();

            doReturn(newTag)
                    .when(tagMapper).requestTagDtoToTag(requestTag);
            doReturn(savedTag)
                    .when(tagRepository).save(newTag);
            doReturn(responseTag)
                    .when(tagMapper).tagToResponseTagDto(savedTag);

            ResponseTagDto actualResponse = tagService.create(requestTag);

            assertThat(actualResponse).isEqualTo(responseTag);
        }

        @Test
        void checkCreateThrow() {
            TestTagBuilder tagBuilder = builder().build();
            RequestTagDto requestTag = tagBuilder.getRequestTag();
            Tag tagWithoutId = tagBuilder.getTagWithoutId();

            doReturn(tagWithoutId)
                    .when(tagMapper).requestTagDtoToTag(requestTag);
            doThrow(DataIntegrityViolationException.class)
                    .when(tagRepository).save(tagWithoutId);

            assertThatThrownBy(() -> tagService.create(requestTag))
                    .isInstanceOf(TagExistsException.class);
        }
    }

    @Nested
    class Update {

        @Test
        void checkUpdate() {
            TestTagBuilder tBuilder = builder().build();
            RequestTagDto requestTag = tBuilder.getRequestTag();
            ResponseTagDto expectedDto = tBuilder.getResponseTag();
            Tag tagWithoutId = tBuilder.getTagWithoutId();
            Tag tag = tBuilder.getTag();
            Long id = tag.getId();

            doReturn(tagWithoutId)
                    .when(tagMapper).requestTagDtoToTag(requestTag);
            doReturn(tag)
                    .when(tagRepository).save(tagWithoutId);
            doReturn(expectedDto)
                    .when(tagMapper).tagToResponseTagDto(tag);

            ResponseTagDto actualDto = tagService.update(id, requestTag);

            assertThat(actualDto).isEqualTo(expectedDto);
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
        TestTagBuilder tagBuilder = builder().build();
        Optional<Tag> optionalTag = tagBuilder.getOptional();
        Tag tag = tagBuilder.getTag();
        ResponseTagDto expectedDto = tagBuilder.getResponseTag();

        doReturn(optionalTag)
                .when(tagRepository).findTheMostWidelyTag();
        doReturn(expectedDto)
                .when(tagMapper).tagToResponseTagDto(tag);

        ResponseTagDto actualDto = tagService.findMostWidelyTag();

        assertThat(actualDto).isEqualTo(expectedDto);
    }

    @Test
    void checkFindMostPopularTagThrows() {
        doReturn(Optional.empty())
                .when(tagRepository).findTheMostWidelyTag();

        assertThatThrownBy(() -> tagService.findMostWidelyTag())
                .isInstanceOf(TagNotFoundException.class);
    }
}
