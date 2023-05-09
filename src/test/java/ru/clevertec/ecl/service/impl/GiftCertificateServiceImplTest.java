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
import ru.clevertec.ecl.pageable.Filter;
import ru.clevertec.ecl.pageable.PageDto;
import ru.clevertec.ecl.pageable.Patch;
import ru.clevertec.ecl.patcher.Patcher;
import ru.clevertec.ecl.service.TagNamesService;
import ru.clevertec.ecl.util.CertificateBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

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

    private final CertificateBuilder cBuilder = CertificateBuilder.builder().build();

    @Nested
    class FindById {

        @Test
        void checkFindById() {
            Optional<GiftCertificate> optionalEntity = cBuilder.getOptionalEntity();
            ResponseGiftCertificateDto expected = cBuilder.getResponseDto();
            Long id = cBuilder.getId();

            doReturn(optionalEntity).when(repository).findById(id);

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
            GiftCertificate entity1 = cBuilder.getEntity();
            ResponseGiftCertificateDto responseDto1 = cBuilder.getResponseDto();
            CertificateBuilder builder1 = cBuilder.toBuilder().id(2L).build();
            GiftCertificate entity2 = builder1.getEntity();
            ResponseGiftCertificateDto responseDto2 = builder1.getResponseDto();
            Pageable pageable = Pageable.unpaged();
            PageDto<ResponseGiftCertificateDto> dtos = PageDto.of(new PageImpl<>(List.of(responseDto1, responseDto2)));

            Page<GiftCertificate> page = new PageImpl<>(List.of(entity1, entity2));

            doReturn(page).when(repository).findAll(pageable);

            Page<ResponseGiftCertificateDto> actual = service.findAll(pageable);

            assertThat(actual).isEqualTo(dtos);
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
            CertificateBuilder build = CertificateBuilder.builder().build();
            GiftCertificate entity = build.getEntity();
            GiftCertificate newEntity = build.toBuilder()
                    .id(null)
                    .lastUpdateDate(null)
                    .createDate(null)
                    .build().getEntity();
            newEntity.getTags().forEach(tag -> tag.setId(null));

            RequestGiftCertificateDto requestDto = build.getRequestDto();

            doReturn(List.of())
                    .when(tagService)
                    .findByNameIn(build.getTags().stream()
                            .map(Tag::getName)
                            .collect(Collectors.toSet()));
            doReturn(entity)
                    .when(repository).save(argThat(arg ->
                            Objects.isNull(arg.getId()) &&
                                    Objects.isNull(arg.getCreateDate()) &&
                                    Objects.isNull(arg.getLastUpdateDate()) &&
                                    Objects.equals(newEntity.getName(), arg.getName()) &&
                                    Objects.equals(newEntity.getPrice(), arg.getPrice())
                    ));

            service.create(requestDto);

            verify(repository, times(1)).save(entity);
        }

        @Test
        void checkCreateWithoutNewTag() {
            GiftCertificate entity = cBuilder.toBuilder().tags(new ArrayList<>()).build().getEntity();
            GiftCertificate newEntity = cBuilder.toBuilder()
                    .id(null)
                    .lastUpdateDate(null)
                    .createDate(null)
                    .build().getEntity();

            RequestGiftCertificateDto requestDto = cBuilder.getRequestDto();
            Set<String> names = newEntity.getTags().stream()
                    .map(Tag::getName)
                    .collect(Collectors.toSet());
            AtomicLong id = new AtomicLong(1);
            List<ResponseTagDto> tags = names.stream()
                    .map(n -> new ResponseTagDto(id.getAndIncrement(), n))
                    .collect(Collectors.toList());

            doReturn(tags)
                    .when(tagService)
                    .findByNameIn(names);
            doReturn(entity)
                    .when(repository).save(argThat(arg ->
                            Objects.isNull(arg.getId()) &&
                                    Objects.isNull(arg.getCreateDate()) &&
                                    Objects.isNull(arg.getLastUpdateDate()) &&
                                    Objects.equals(newEntity.getName(), arg.getName()) &&
                                    Objects.equals(newEntity.getPrice(), arg.getPrice()) &&
                                    arg.getTags().isEmpty()
                    ));

            service.create(requestDto);

            verify(repository, times(1)).save(argThat(arg -> arg.getTags().size() == 2));
        }
    }

    @Nested
    class Update {

        private final Long id = cBuilder.getId();

        @Test
        void checkUpdateName() {
            Optional<GiftCertificate> entity = cBuilder.getOptionalEntity();
            CertificateBuilder updateBuilder = cBuilder.toBuilder()
                    .name("Updated name")
                    .build();

            doReturn(entity).when(repository).findById(id);

            service.update(id, updateBuilder.getRequestDto());

            verify(repository).saveAndFlush(
                    argThat(argument -> Objects.equals(argument.getName(), updateBuilder.getName())));
        }

        @Test
        void checkNotUpdateCreateDate() {
            Optional<GiftCertificate> entity = cBuilder.getOptionalEntity();
            doReturn(entity).when(repository).findById(id);
            LocalDateTime expected = cBuilder.getCreateDate();

            service.update(id, cBuilder.getRequestDto());

            verify(repository).saveAndFlush(
                    argThat(argument -> Objects.equals(argument.getCreateDate(), expected)));
        }

        @Test
        void checkUpdateLastUpdateDate() {
            Optional<GiftCertificate> entity = cBuilder.getOptionalEntity();
            doReturn(entity).when(repository).findById(id);
            LocalDateTime expected = cBuilder.getLastUpdateDate();

            service.update(id, cBuilder.getRequestDto());

            verify(repository).saveAndFlush(
                    argThat(argument -> argument.getLastUpdateDate().isEqual(expected)));
        }

        @Test
        void checkUpdatePrice() {
            Optional<GiftCertificate> entity = cBuilder.getOptionalEntity();
            CertificateBuilder updateBuilder = cBuilder.toBuilder()
                    .price(BigDecimal.valueOf(99.91))
                    .build();

            doReturn(entity).when(repository).findById(id);

            service.update(id, updateBuilder.getRequestDto());

            verify(repository).saveAndFlush(
                    argThat(argument -> Objects.equals(argument.getPrice(), updateBuilder.getPrice())));
        }

        @Test
        void checkUpdateDuration() {
            Optional<GiftCertificate> entity = cBuilder.getOptionalEntity();
            CertificateBuilder updateBuilder = cBuilder.toBuilder()
                    .duration(5)
                    .build();

            doReturn(entity).when(repository).findById(id);

            service.update(id, updateBuilder.getRequestDto());

            verify(repository).saveAndFlush(
                    argThat(argument -> Objects.equals(argument.getDuration(), updateBuilder.getDuration())));
        }

        @Test
        void updateDescription() {
            Optional<GiftCertificate> entity = cBuilder.getOptionalEntity();
            CertificateBuilder certificateBuilder = cBuilder.toBuilder()
                    .description("Updated description")
                    .build();

            doReturn(entity).when(repository).findById(id);

            service.update(id, certificateBuilder.getRequestDto());

            verify(repository).saveAndFlush(argThat(argument -> Objects.equals(argument.getDescription(), certificateBuilder.getDescription())));
        }

        @Test
        void updateTag() {
            Optional<GiftCertificate> entity = cBuilder.getOptionalEntity();
            CertificateBuilder certificateBuilder = cBuilder.toBuilder()
                    .tags(Stream.of(
                                    Tag.builder().name("new1").build(),
                                    Tag.builder().name("new2").build(),
                                    Tag.builder().name("new3").build())
                            .collect(Collectors.toList()))
                    .build();
            RequestGiftCertificateDto requestDto = certificateBuilder.getRequestDto();
            Set<String> tagNames = Set.of("new1", "new2", "new3");
            List<ResponseTagDto> existedTag = Stream.of(
                    new ResponseTagDto(10L, "new1"),
                    new ResponseTagDto(11L, "new2")
            ).collect(Collectors.toList());

            doReturn(existedTag).when(tagService).findByNameIn(tagNames);
            doReturn(entity).when(repository).findById(id);

            service.update(id, requestDto);

            List<Long> listIds = Stream.concat(existedTag.stream().map(ResponseTagDto::id),
                            cBuilder.getTags().stream().map(Tag::getId))
                    .toList();

            verify(repository).saveAndFlush(argThat(
                    arg -> listIds.containsAll(
                            arg.getTags().stream()
                            .map(Tag::getId)
                            .toList())));
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
        CertificateBuilder build2 = cBuilder.toBuilder().name("11").build();
        CertificateBuilder build3 = cBuilder.toBuilder().name("111").build();

        Page<GiftCertificate> returned = new PageImpl<>(List.of(build1.getEntity(), build2.getEntity(), build3.getEntity()));
        Filter filter = Filter.builder().part("1").build();
        Pageable pageable = Pageable.unpaged();

        doReturn(returned).when(repository)
                .findByTagNameAndPartOfNameOrDescription(null, "1", pageable);

        List<ResponseGiftCertificateDto> expected = List.of(build1.getResponseDto(), build2.getResponseDto(), build3.getResponseDto());

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
        Long id = entity.getId();
        Patch patch = new Patch("description", "new desc");

        doReturn(optionalEntity)
                .when(repository).findById(id);

        service.patch(entity.getId(), patch);

        verify(repository, times(1)).save(entity);
        verify(patcher, times(1)).applyPatch(entity, patch);
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