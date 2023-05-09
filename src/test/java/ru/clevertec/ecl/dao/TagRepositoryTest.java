package ru.clevertec.ecl.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.util.StaticPostgresTestContainer;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Sql("classpath:sql_script/popular_tag.sql")
class TagRepositoryTest extends StaticPostgresTestContainer {

    @Autowired
    private TagRepository repository;

    @Test
    void findTheMostPopularTag() {
        Tag theMostPopularTag = repository.findTheMostPopularTag()
                .orElseThrow();

        assertThat(theMostPopularTag.getId()).isEqualTo(3);
    }
}