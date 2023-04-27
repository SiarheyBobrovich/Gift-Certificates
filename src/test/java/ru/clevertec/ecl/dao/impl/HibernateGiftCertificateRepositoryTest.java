package ru.clevertec.ecl.dao.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.pageable.Filter;
import ru.clevertec.ecl.util.StaticPostgresTestContainer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HibernateGiftCertificateRepositoryTest extends StaticPostgresTestContainer {

    private HibernateGiftCertificateRepository repository;
    private SessionFactory sessionFactory;

    @BeforeEach
    void setUp() {
        sessionFactory = sessionFactory();
        sessionFactory.getCurrentSession().getTransaction().begin();
        repository = new HibernateGiftCertificateRepository(sessionFactory);
    }

    @AfterEach
    void close() {
        sessionFactory.close();
    }

    @Test
    void checkSave() {
        LocalDateTime now = LocalDateTime.now();
        GiftCertificate certificate = GiftCertificate.builder()
                .name("HibernateGiftCertificate")
                .description("Hibernate gift certificate")
                .duration(2)
                .price(BigDecimal.valueOf(5.55))
                .createDate(now)
                .lastUpdateDate(now)
                .build();

        GiftCertificate save = repository.save(certificate);

        Long id = save.getId();

        assertThat(id).isNotNull();
    }

    @Test
    void checkSaveWithTags() {
        LocalDateTime now = LocalDateTime.now();
        GiftCertificate certificate = GiftCertificate.builder()
                .name("HibernateGiftCertificate")
                .description("Hibernate gift certificate")
                .duration(2)
                .price(BigDecimal.valueOf(5.55))
                .createDate(now)
                .lastUpdateDate(now)
                .tags(List.of(
                        Tag.builder().id(1L).name("#1").build(),
                        Tag.builder().id(2L).name("#2").build()))
                .build();

        GiftCertificate save = repository.save(certificate);
        Long id = save.getId();

        assertThat(id).isNotNull();
    }

    @Test
    void checkSaveWithNewTags() {
        LocalDateTime now = LocalDateTime.now();
        GiftCertificate certificate = GiftCertificate.builder()
                .name("HibernateGiftCertificate")
                .description("Hibernate gift certificate")
                .duration(2)
                .price(BigDecimal.valueOf(5.55))
                .createDate(now)
                .lastUpdateDate(now)
                .tags(List.of(
                        Tag.builder().name("#5 tag").build(),
                        Tag.builder().name("#6 tag").build()))
                .build();

        GiftCertificate save = repository.save(certificate);
        Long id = save.getId();

        assertThat(id).isNotNull();
    }

    @Test
    void checkSaveWithCurrentAndNewTags() {
        LocalDateTime now = LocalDateTime.now();
        GiftCertificate certificate = GiftCertificate.builder()
                .name("HibernateGiftCertificate")
                .description("Hibernate gift certificate")
                .duration(2)
                .price(BigDecimal.valueOf(5.55))
                .createDate(now)
                .lastUpdateDate(now)
                .build();
        certificate.addTag(Tag.builder().name("#1").build());
        certificate.addTag(Tag.builder().name("#2 tag").build());

        GiftCertificate save = repository.save(certificate);
        Long id = save.getId();

        assertThat(id).isNotNull();
    }

    @Test
    void checkUpdate() {
        String updatedName = "Update name";
        GiftCertificate giftCertificate = repository.findById(3L).orElseThrow();

        giftCertificate.setName(updatedName);
        repository.update(giftCertificate);
        GiftCertificate updated = repository.findById(giftCertificate.getId()).orElseThrow();
        String name = updated.getName();

        assertThat(name).isEqualTo(updatedName);
    }

    @Test
    void checkUpdateTags() {
        Long id = 3L;
        GiftCertificate giftCertificate = repository.findById(id).orElseThrow();
        giftCertificate.addTag(Tag.builder().name("#14").build());

        List<String> expected = giftCertificate.getTags().stream()
                .map(Tag::getName)
                .toList();

        repository.update(giftCertificate);
        GiftCertificate updated = repository.findById(id).orElseThrow();
        List<String> tags = updated.getTags().stream()
                .map(Tag::getName)
                .toList();

        assertThat(tags).isEqualTo(expected);
    }

    @Test
    void checkDelete() {
        repository.delete(4L);
        Optional<GiftCertificate> expected = repository.findById(4L);

        assertThat(expected).isEmpty();
    }

    @Nested
    class Find {

        @Test
        void checkFindById() {
            GiftCertificate current = repository.findById(1L).orElseThrow();

            GiftCertificate expected = GiftCertificate.builder()
                    .id(1L)
                    .name("first")
                    .description("one certificate")
                    .price(BigDecimal.valueOf(1.11))
                    .duration(11)
                    .createDate(current.getCreateDate())
                    .lastUpdateDate(current.getLastUpdateDate())
                    .build();
            current.setTags(null);

            assertThat(current).isEqualTo(expected);
        }

        @Test
        void checkFindAll() {
            List<Long> ids = repository.findAll().stream()
                    .map(GiftCertificate::getId)
                    .toList();

            assertThat(ids).containsAll(List.of(1L, 2L, 3L));
        }

        @Test
        @DisplayName("find by Tag '#5'")
        void checkFindByPart() {
            List<Long> expectedIdList = List.of(1L);
            Filter firstTag = Filter.builder().tagName("#5").build();

            List<GiftCertificate> byPart = repository.findByFilter(firstTag);
            List<Long> actualIdList = byPart.stream().map(GiftCertificate::getId).toList();

            assertThat(actualIdList).isEqualTo(expectedIdList);
        }

        @Test
        @DisplayName("find by Tag '#6'")
        void checkFindByPartTag() {
            List<Long> expectedId = List.of(1L, 2L);

            Filter secondTag = Filter.builder().tagName("#6").build();
            List<GiftCertificate> byPart = repository.findByFilter(secondTag);
            List<Long> actualIdList = byPart.stream().map(GiftCertificate::getId).toList();

            assertThat(actualIdList).isEqualTo(expectedId);
        }

        @Test
        @DisplayName("find by Tag '#6', order by")
        void checkFindByPartTagOrderBy() {
            List<Long> expectedId = List.of(1L, 2L);

            Filter secondTag = Filter.builder().tagName("#6").build();

            List<String> sort = List.of("name_asc", "createDate_desc");
            secondTag.addSortFieldName(sort);

            List<GiftCertificate> byPart = repository.findByFilter(secondTag);
            List<Long> actualIdList = byPart.stream().map(GiftCertificate::getId).toList();

            assertThat(actualIdList).isEqualTo(expectedId);
        }

        @Test
        @DisplayName("find by Tag '#2', And part 'first'")
        void checkFindByPartTagAndPart() {
            List<Long> expectedId = List.of(1L);

            Filter secondTag = Filter.builder().tagName("#2")
                    .partOfNameOrDescription("first")
                    .build();

            List<String> sort = List.of("name_asc", "createDate_desc");
            secondTag.addSortFieldName(sort);

            List<GiftCertificate> byPart = repository.findByFilter(secondTag);
            List<Long> actualIdList = byPart.stream().map(GiftCertificate::getId).toList();

            assertThat(actualIdList).isEqualTo(expectedId);
        }
    }
}
