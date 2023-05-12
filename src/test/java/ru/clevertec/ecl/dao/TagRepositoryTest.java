package ru.clevertec.ecl.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.util.PostgresTestContainer;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Sql("classpath:sql_script/popular_tag.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TagRepositoryTest extends PostgresTestContainer {

    @Autowired
    private TagRepository repository;

    @Test
    void findTheMostWidelyTag() {
        Tag theMostPopularTag = repository.findTheMostWidelyTag()
                .orElseThrow();

        assertThat(theMostPopularTag.getId()).isEqualTo(3);
    }
}
