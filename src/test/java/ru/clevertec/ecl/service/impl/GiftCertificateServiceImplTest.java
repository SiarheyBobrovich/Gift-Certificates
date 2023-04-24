package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.dao.GiftCertificateRepository;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.util.CertificateBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @InjectMocks
    private GiftCertificateServiceImpl service;

    @Mock
    private GiftCertificateRepository repository;

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Nested
    class Create {

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
        void checkCreateCreateDateNotNull() {
            CertificateBuilder build = CertificateBuilder.builder().build();
            RequestGiftCertificateDto requestDto = build.getRequestDto();

            service.create(requestDto);

            verify(repository).save(argThat(x -> x.getCreateDate() != null));
        }

        @Test
        void checkCreateCreateDateNearNow() {
            CertificateBuilder build = CertificateBuilder.builder().build();
            RequestGiftCertificateDto requestDto = build.getRequestDto();

            LocalDateTime before = LocalDateTime.now();
            service.create(requestDto);
            LocalDateTime after = LocalDateTime.now();

            verify(repository).save(argThat(x -> {
                LocalDateTime createDate = x.getCreateDate();
                return createDate.isEqual(LocalDateTime.now()) ||
                        (createDate.isAfter(before) && createDate.isBefore(after));
            }));
        }

        @Test
        void checkCreateLastUpdateDateNotNull() {
            CertificateBuilder build = CertificateBuilder.builder().build();
            RequestGiftCertificateDto requestDto = build.getRequestDto();

            service.create(requestDto);

            verify(repository).save(argThat(x -> x.getLastUpdateDate() != null));
        }

        @Test
        void checkCreateLastUpdateDateNearNow() {
            CertificateBuilder build = CertificateBuilder.builder().build();
            RequestGiftCertificateDto requestDto = build.getRequestDto();

            LocalDateTime before = LocalDateTime.now();
            service.create(requestDto);
            LocalDateTime after = LocalDateTime.now();

            verify(repository).save(argThat(x -> {
                LocalDateTime lastUpdateDate = x.getLastUpdateDate();
                return lastUpdateDate.isEqual(LocalDateTime.now()) ||
                        (lastUpdateDate.isAfter(before) && lastUpdateDate.isBefore(after));
            }));
        }

        @Test
        void checkCreatePrice() {
            CertificateBuilder build = CertificateBuilder.builder().build();
            RequestGiftCertificateDto requestDto = build.getRequestDto();

            service.create(requestDto);

            verify(repository).save(argThat(x -> Objects.equals(x.getPrice(), requestDto.price())));
        }
    }

    @Test
    void update() {
    }

    @Test
    void findByPartOfNameOrDescription() {
    }
}