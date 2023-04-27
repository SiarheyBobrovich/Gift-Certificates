package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.dao.GiftCertificateRepository;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.pageable.Filter;
import ru.clevertec.ecl.util.CertificateBuilder;

import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @InjectMocks
    private GiftCertificateServiceImpl service;

    @Mock
    private GiftCertificateRepository repository;

    @Mock
    private Validator validator;

    private CertificateBuilder builder = CertificateBuilder.builder().build();

    @Nested
    class FindById {

        @Test
        void checkFindById() {
            Optional<GiftCertificate> optionalEntity = builder.getOptionalEntity();
            ResponseGiftCertificateDto expected = builder.getResponseDto();
            Long id = builder.getId();

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
            GiftCertificate entity1 = builder.getEntity();
            ResponseGiftCertificateDto responseDto1 = builder.getResponseDto();
            CertificateBuilder builder1 = builder.toBuilder().id(2L).build();
            GiftCertificate entity2 = builder1.getEntity();
            ResponseGiftCertificateDto responseDto2 = builder1.getResponseDto();
            List<ResponseGiftCertificateDto> expected = List.of(responseDto1, responseDto2);

            doReturn(List.of(entity1, entity2)).when(repository).findAll();

            List<ResponseGiftCertificateDto> actual = service.findAll();

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void checkFindAllEmpty() {
            doReturn(List.of()).when(repository).findAll();

            List<ResponseGiftCertificateDto> actual = service.findAll();

            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class Create {

        @BeforeEach
        void setUp() {
            doReturn(Set.of()).when(validator).validate(any());
        }

        @Test
        void checkCreateId() {
            CertificateBuilder build = CertificateBuilder.builder().build();
            RequestGiftCertificateDto requestDto = build.getRequestDto();


            service.create(requestDto);

            verify(repository).save(argThat(x ->
                    Objects.isNull(x.getId())));
        }

        @Test
        void checkCreateName() {
            CertificateBuilder build = CertificateBuilder.builder().build();
            RequestGiftCertificateDto requestDto = build.getRequestDto();

            service.create(requestDto);

            verify(repository).save(argThat(x ->
                    Objects.equals(x.getName(), requestDto.name())));
        }

        @Test
        void checkCreateDescription() {
            CertificateBuilder build = CertificateBuilder.builder().build();
            RequestGiftCertificateDto requestDto = build.getRequestDto();

            service.create(requestDto);

            verify(repository).save(argThat(x ->
                    Objects.equals(x.getDescription(), requestDto.description())));
        }

        @Test
        void checkCreateDuration() {
            CertificateBuilder build = CertificateBuilder.builder().build();
            RequestGiftCertificateDto requestDto = build.getRequestDto();

            service.create(requestDto);

            verify(repository).save(argThat(x ->
                    Objects.equals(x.getDuration(), requestDto.duration())));
        }

        @Test
        void checkCreateTags() {
            CertificateBuilder build = CertificateBuilder.builder().build();
            RequestGiftCertificateDto requestDto = build.getRequestDto();
            List<Tag> tags = requestDto.tags().stream()
                    .map(x -> Tag.builder().name(x.name()).build())
                    .toList();

            service.create(requestDto);

            verify(repository).save(argThat(x ->
                    Objects.equals(x.getTags(), tags)));
        }

        @Test
        void checkCreateCreateDateIsNull() {
            CertificateBuilder build = CertificateBuilder.builder().build();
            RequestGiftCertificateDto requestDto = build.getRequestDto();

            service.create(requestDto);

            verify(repository).save(argThat(x -> Objects.isNull(x.getCreateDate())));
        }

        @Test
        void checkCreateLastUpdateDateIsNull() {
            CertificateBuilder build = CertificateBuilder.builder().build();
            RequestGiftCertificateDto requestDto = build.getRequestDto();

            service.create(requestDto);

            verify(repository).save(argThat(x -> Objects.isNull(x.getLastUpdateDate())));
        }

        @Test
        void checkCreatePrice() {
            CertificateBuilder build = CertificateBuilder.builder().build();
            RequestGiftCertificateDto requestDto = build.getRequestDto();

            service.create(requestDto);

            verify(repository).save(argThat(x -> Objects.equals(x.getPrice(), requestDto.price())));
        }
    }

    @Nested
    class Update {

        private final Optional<GiftCertificate> entity = builder.getOptionalEntity();
        private final Long id = entity.orElseThrow().getId();

        @Test
        void checkUpdateName() {
            CertificateBuilder updateBuilder = builder.toBuilder()
                    .name("Updated name")
                    .description("Updated description")
                    .duration(5)
                    .price(BigDecimal.valueOf(99.91))
                    .build();

            doReturn(entity).when(repository).findById(id);

            service.update(id, updateBuilder.getRequestDto());

            verify(repository).update(argThat(argument -> Objects.equals(argument.getName(), updateBuilder.getName())));
        }

        @Test
        void checkNotUpdateCreateDate() {
            doReturn(entity).when(repository).findById(id);
            LocalDateTime expected = builder.getCreateDate();

            service.update(id, builder.getRequestDto());

            verify(repository).update(argThat(argument -> Objects.equals(argument.getCreateDate(), expected)));
        }

        @Test
        void checkUpdateLastUpdateDate() {
            doReturn(entity).when(repository).findById(id);
            LocalDateTime expected = builder.getLastUpdateDate();

            service.update(id, builder.getRequestDto());

            verify(repository).update(argThat(argument -> argument.getLastUpdateDate().isEqual(expected)));
        }

        @Test
        void checkUpdatePrice() {
            CertificateBuilder updateBuilder = builder.toBuilder()
                    .price(BigDecimal.valueOf(99.91))
                    .build();

            doReturn(entity).when(repository).findById(id);

            service.update(id, updateBuilder.getRequestDto());

            verify(repository).update(argThat(argument -> Objects.equals(argument.getPrice(), updateBuilder.getPrice())));
        }

        @Test
        void checkUpdateDuration() {
            CertificateBuilder updateBuilder = builder.toBuilder()
                    .duration(5)
                    .build();

            doReturn(entity).when(repository).findById(id);

            service.update(id, updateBuilder.getRequestDto());

            verify(repository).update(argThat(argument -> Objects.equals(argument.getDuration(), updateBuilder.getDuration())));
        }

        @Test
        void updateDescription() {
            CertificateBuilder updateBuilder = builder.toBuilder()
                    .description("Updated description")
                    .build();

            doReturn(entity).when(repository).findById(id);

            service.update(id, updateBuilder.getRequestDto());

            verify(repository).update(argThat(argument -> Objects.equals(argument.getDescription(), updateBuilder.getDescription())));
        }
    }

    @Test
    void findByPartOfNameOrDescription() {
        CertificateBuilder build1 = builder.toBuilder().name("1").build();
        CertificateBuilder build2 = builder.toBuilder().name("11").build();
        CertificateBuilder build3 = builder.toBuilder().name("111").build();

        List<GiftCertificate> returned = List.of(build1.getEntity(), build2.getEntity(), build3.getEntity());
        Filter filter = Filter.builder().partOfNameOrDescription("1").build();

        doReturn(returned).when(repository).findByFilter(filter);

        List<ResponseGiftCertificateDto> expected = List.of(build1.getResponseDto(), build2.getResponseDto(), build3.getResponseDto());

        List<ResponseGiftCertificateDto> actual = service.findByFilter(filter);

        assertThat(actual).isEqualTo(expected);
    }
}