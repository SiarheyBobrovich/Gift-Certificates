package ru.clevertec.ecl.dao.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.util.PostgresTestContainer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class HibernateGiftCertificateRepositoryTest extends PostgresTestContainer {

    private HibernateGiftCertificateRepository repository;

    @BeforeEach
    void setUp() {
        SessionFactory sessionFactory = sessionFactory();
        repository = new HibernateGiftCertificateRepository(sessionFactory);
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
                        Tag.builder().id(1L).name("first tag").build(),
                        Tag.builder().id(2L).name("second tag").build()))
                .build();

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
    void checkDelete() {
        repository.delete(4L);
        Optional<GiftCertificate> expected = repository.findById(4L);

        assertThat(expected).isEmpty();
    }

    @Test
    void checkFindById() {
        GiftCertificate current = repository.findById(1L).orElseThrow();

        GiftCertificate expected = GiftCertificate.builder()
                .id(1L)
                .name("first")
                .description("first certificate")
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
}
