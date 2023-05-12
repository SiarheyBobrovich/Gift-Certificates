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
import ru.clevertec.ecl.dao.GiftCertificateRepository;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.pageable.Filter;
import ru.clevertec.ecl.pageable.PageDto;
import ru.clevertec.ecl.pageable.Patch;
import ru.clevertec.ecl.patcher.Patcher;
import ru.clevertec.ecl.service.TagNamesService;
import ru.clevertec.ecl.util.CertificateBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @InjectMocks
    private GiftCertificateServiceImpl service;

    @Mock
    private GiftCertificateRepository repository;

    @Mock
    private TagNamesService tagService;

    @Mock
    private Patcher<GiftCertificate> patcher;

    @Mock
    private GiftCertificateMapper mapper;

    private final CertificateBuilder cBuilder = CertificateBuilder.builder().build();

    @Nested
    class FindById {

        @Test
        void checkFindById() {
            Optional<GiftCertificate> optionalEntity = cBuilder.getOptionalEntity();
            GiftCertificate certificate = cBuilder.getEntity();
            ResponseGiftCertificateDto expected = cBuilder.getResponseDto();
            Long id = cBuilder.getId();

            doReturn(optionalEntity)
                    .when(repository).findById(id);
            doReturn(expected)
                    .when(mapper).giftCertificateToResponseGiftCertificateDto(certificate);

            ResponseGiftCertificateDto actual = service.findById(id);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void checkFindByIdThrow() {
            Long id = 1L;
            doReturn(Optional.empty()).when(repository).findById(id);

            assertThatThrownBy(() -> service.findById(id)).isInstanceOf(EntityNotFoundException.class);
        }
    }

    @Nested
    class FindAll {

        @Test
        void checkFindAll() {
            CertificateBuilder builder1 = cBuilder.toBuilder().id(2L).build();
            ResponseGiftCertificateDto responseDto1 = cBuilder.getResponseDto();
            ResponseGiftCertificateDto responseDto2 = builder1.getResponseDto();
            GiftCertificate entity1 = cBuilder.getEntity();
            GiftCertificate entity2 = builder1.getEntity();
            Pageable pageable = Pageable.unpaged();
            PageDto<ResponseGiftCertificateDto> expected = PageDto.of(new PageImpl<>(List.of(responseDto1, responseDto2)));
            Page<GiftCertificate> repositoryPage = new PageImpl<>(List.of(entity1, entity2));

            doReturn(responseDto1)
                    .when(mapper).giftCertificateToResponseGiftCertificateDto(entity1);
            doReturn(responseDto2)
                    .when(mapper).giftCertificateToResponseGiftCertificateDto(entity2);
            doReturn(repositoryPage)
                    .when(repository).findAll(pageable);

            Page<ResponseGiftCertificateDto> actual = service.findAll(pageable);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void checkFindAllEmpty() {
            Pageable pageable = Pageable.ofSize(10);
            PageImpl<GiftCertificate> page = new PageImpl<>(List.of(), pageable, 0);
            doReturn(page).when(repository).findAll(pageable);

            Page<ResponseGiftCertificateDto> actual = service.findAll(pageable);

            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class Create {

        @Test
        void checkCreateWithNewTag() {
            ResponseGiftCertificateDto expectedCertificate = cBuilder.getResponseDto();
            RequestGiftCertificateDto requestDto = cBuilder.getRequestDto();
            GiftCertificate saveWithoutSavedTags = cBuilder.getEntity();
            GiftCertificate savedWithSavedTags = cBuilder.getEntity();
            GiftCertificate newEntity = cBuilder.toBuilder()
                    .id(null)
                    .lastUpdateDate(null)
                    .createDate(null)
                    .build()
                    .getEntity();
            newEntity.getTags().forEach(tag -> tag.setId(null));

            doReturn(newEntity)
                    .when(mapper).requestGiftCertificateDtoToGiftCertificate(requestDto);
            doReturn(expectedCertificate)
                    .when(mapper).giftCertificateToResponseGiftCertificateDto(saveWithoutSavedTags);
            doReturn(List.of())
                    .when(tagService)
                    .findByNameIn(cBuilder.getTags().stream()
                            .map(Tag::getName)
                            .collect(Collectors.toSet()));
            doReturn(saveWithoutSavedTags)
                    .when(repository).save(newEntity);
            doReturn(savedWithSavedTags)
                    .when(repository).save(saveWithoutSavedTags);

            ResponseGiftCertificateDto actualCertificate = service.create(requestDto);

            assertThat(actualCertificate).isEqualTo(expectedCertificate);
        }

        @Test
        void checkCreateWithoutNewTag() {
            RequestGiftCertificateDto requestDto = cBuilder.getRequestDto();
            ResponseGiftCertificateDto expected = cBuilder.getResponseDto();
            GiftCertificate certificateWithoutTags = cBuilder.toBuilder().tags(new ArrayList<>()).build().getEntity();
            GiftCertificate requestCertificate = cBuilder.toBuilder()
                    .id(null)
                    .lastUpdateDate(null)
                    .createDate(null)
                    .tags(cBuilder.getTags().stream()
                            .map(t -> new Tag(null, t.getName()))
                            .collect(Collectors.toList()))
                    .build()
                    .getEntity();
            GiftCertificate saved = cBuilder.getEntity();

            Set<String> names = requestCertificate.getTags().stream()
                    .map(Tag::getName)
                    .collect(Collectors.toSet());
            List<ResponseTagDto> responseTagDtos = cBuilder.getTags().stream()
                    .map(tag -> new ResponseTagDto(tag.getId(), tag.getName()))
                    .collect(Collectors.toList());

            doReturn(requestCertificate)
                    .when(mapper).requestGiftCertificateDtoToGiftCertificate(requestDto);
            doReturn(certificateWithoutTags)
                    .when(repository).save(requestCertificate);
            doReturn(responseTagDtos)
                    .when(tagService).findByNameIn(names);
            doReturn(saved)
                    .when(repository).save(certificateWithoutTags);
            doReturn(expected)
                    .when(mapper).giftCertificateToResponseGiftCertificateDto(saved);

            ResponseGiftCertificateDto actual = service.create(requestDto);

            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class Update {

        private final Long id = cBuilder.getId();

        @Test
        void checkUpdate() {
            Optional<GiftCertificate> entity = cBuilder.getOptionalEntity();
            GiftCertificate certificate = cBuilder.getEntity();
            CertificateBuilder updateBuilder = cBuilder.toBuilder()
                    .name("Updated name")
                    .price(BigDecimal.valueOf(1_000_000L))
                    .duration(1_000_000)
                    .description("updated description")
                    .build();
            GiftCertificate updatedCertificate = updateBuilder.getEntity();
            RequestGiftCertificateDto requestDto = updateBuilder.getRequestDto();
            ResponseGiftCertificateDto expectedDto = updateBuilder.getResponseDto();

            doReturn(entity)
                    .when(repository).findById(id);
            doReturn(updatedCertificate)
                    .when(mapper).merge(certificate, requestDto);
            doReturn(updatedCertificate)
                    .when(repository).saveAndFlush(updatedCertificate);
            doReturn(expectedDto)
                    .when(mapper).giftCertificateToResponseGiftCertificateDto(updatedCertificate);

            ResponseGiftCertificateDto actualDto = service.update(id, requestDto);

            assertThat(actualDto).isEqualTo(expectedDto);
        }

        @Test
        void updateTag() {
            Optional<GiftCertificate> optionalCurrentCertificate = cBuilder.getOptionalEntity();
            GiftCertificate currentCertificate = cBuilder.getEntity();
            Tag tag1 = new Tag(10L, "new1");
            Tag tag2 = new Tag(11L, "new2");
            CertificateBuilder updateBuilder = cBuilder.toBuilder()
                    .tags(Stream.of(
                                    tag1,
                                    tag2,
                                    Tag.builder().id(12L).name("new3").build())
                            .collect(Collectors.toList()))
                    .build();
            RequestGiftCertificateDto requestDto = updateBuilder.getRequestDto();
            Set<String> tagNames = Set.of("new1", "new2", "new3");
            List<ResponseTagDto> existedTag = Stream.of(
                    new ResponseTagDto(10L, "new1"),
                    new ResponseTagDto(11L, "new2")
            ).collect(Collectors.toList());
            List<Tag> updatedTags = new ArrayList<>(cBuilder.getTags());
            updatedTags.addAll(List.of(
                    tag1, tag2, new Tag(null, "new3")));
            GiftCertificate updatedCertificate = cBuilder.toBuilder()
                    .tags(updatedTags).build().getEntity();

            ResponseGiftCertificateDto expectedDto = updateBuilder.getResponseDto();

            doReturn(optionalCurrentCertificate)
                    .when(repository).findById(id);
            doReturn(currentCertificate)
                    .when(mapper).merge(currentCertificate, requestDto);
            doReturn(existedTag)
                    .when(tagService).findByNameIn(tagNames);
            doReturn(updatedCertificate)
                    .when(repository).saveAndFlush(argThat(arg ->
                            arg.getTags().size() == 5 &&
                                    Objects.equals(arg.getId(), currentCertificate.getId())));
            doReturn(expectedDto)
                    .when(mapper).giftCertificateToResponseGiftCertificateDto(updatedCertificate);

            ResponseGiftCertificateDto actualDto = service.update(id, requestDto);

            assertThat(actualDto).isEqualTo(expectedDto);

        }

        @Test
        void updateThrows() {
            RequestGiftCertificateDto requestDto = cBuilder.getRequestDto();

            doReturn(Optional.empty())
                    .when(repository).findById(id);

            assertThatThrownBy(() -> service.update(id, requestDto))
                    .isInstanceOf(GiftCertificateNotFoundException.class);
        }
    }

    @Test
    void findByPartOfNameOrDescription() {
        CertificateBuilder build1 = cBuilder.toBuilder().name("1").build();
        CertificateBuilder build2 = cBuilder.toBuilder().id(2L).name("11").build();
        CertificateBuilder build3 = cBuilder.toBuilder().id(3L).name("111").build();
        GiftCertificate certificate1 = build1.getEntity();
        GiftCertificate certificate2 = build2.getEntity();
        GiftCertificate certificate3 = build3.getEntity();
        ResponseGiftCertificateDto certificateDto1 = build1.getResponseDto();
        ResponseGiftCertificateDto certificateDto2 = build2.getResponseDto();
        ResponseGiftCertificateDto certificateDto3 = build3.getResponseDto();

        Page<GiftCertificate> returned = new PageImpl<>(List.of(certificate1, certificate2, certificate3));
        List<ResponseGiftCertificateDto> expected = List.of(certificateDto1, certificateDto2, certificateDto3);
        Filter filter = Filter.builder().part("1").build();
        Pageable pageable = Pageable.unpaged();

        doReturn(certificateDto1)
                .when(mapper).giftCertificateToResponseGiftCertificateDto(certificate1);
        doReturn(certificateDto2)
                .when(mapper).giftCertificateToResponseGiftCertificateDto(certificate2);
        doReturn(certificateDto3)
                .when(mapper).giftCertificateToResponseGiftCertificateDto(certificate3);
        doReturn(returned).when(repository)
                .findByTagNameAndPartOfNameOrDescription(null, "1", pageable);

        Page<ResponseGiftCertificateDto> actual = service.findByFilter(filter, pageable);

        assertThat(actual.getContent()).isEqualTo(expected);
    }

    @Test
    void checkDelete() {
        service.delete(1L);

        verify(repository, times(1))
                .deleteById(1L);
    }

    @Test
    void checkPatch() {
        Optional<GiftCertificate> optionalEntity = cBuilder.getOptionalEntity();
        GiftCertificate entity = cBuilder.getEntity();
        ResponseGiftCertificateDto expected = cBuilder.getResponseDto();
        Long id = entity.getId();
        Patch patch = new Patch("description", "new desc");

        doReturn(optionalEntity)
                .when(repository).findById(id);
        doNothing()
                .when(patcher).applyPatch(entity, patch);
        doReturn(entity)
                .when(repository).save(entity);
        doReturn(expected)
                .when(mapper).giftCertificateToResponseGiftCertificateDto(entity);

        ResponseGiftCertificateDto patched = service.patch(entity.getId(), patch);

        assertThat(patched).isEqualTo(expected);
    }

    @Test
    void checkPatchThrows() {
        Patch patch = new Patch("description", "new desc");

        doReturn(Optional.empty())
                .when(repository).findById(1L);

        assertThatThrownBy(() -> service.patch(1L, patch))
                .isInstanceOf(GiftCertificateNotFoundException.class);
    }
}
