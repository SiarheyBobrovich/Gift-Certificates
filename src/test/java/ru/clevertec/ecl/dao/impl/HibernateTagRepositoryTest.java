package ru.clevertec.ecl.dao.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.util.PostgresTestContainer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HibernateTagRepositoryTest extends PostgresTestContainer {

    private HibernateTagRepository repository;
    private SessionFactory sessionFactory;

    @BeforeEach
    void setUp() {
        sessionFactory = sessionFactory();
        sessionFactory.getCurrentSession().getTransaction().begin();
        repository = new HibernateTagRepository(sessionFactory);
    }

    @AfterEach
    void close() {
        sessionFactory.close();
    }

    @Test
    void checkSave() {
        Tag tag = Tag.builder().name("checkSave").build();
        Tag save = repository.save(tag);
        Long id = save.getId();

        assertThat(id).isNotNull();
    }

    @Test
    void checkUpdate() {
        Tag tag = Tag.builder().id(3L).name("checkUpdate").build();
        Tag updated = repository.update(tag);
        Long id = updated.getId();
        Tag expected = repository.findById(id).orElseThrow();

        assertThat(updated).isEqualTo(expected);
    }

    @Test
    void checkDelete() {
        repository.delete(4L);
        Optional<Tag> tag = repository.findById(4L);

        assertThat(tag).isEmpty();
    }

    @Test
    void checkDeleteWithCross() {
        repository.delete(5L);
        Optional<Tag> tag = repository.findById(5L);

        assertThat(tag).isEmpty();
    }

    @Test
    void checkFindById() {
        Tag expected = Tag.builder().id(1L).name("#1").build();
        Tag tag = repository.findById(1L).orElseThrow();

        assertThat(tag).isEqualTo(expected);
    }

    @Test
    void checkFindAll() {
        List<Tag> tags = repository.findAll();
        tags.forEach(System.out::println);

        assertThat(tags).isNotEmpty();
    }
}
