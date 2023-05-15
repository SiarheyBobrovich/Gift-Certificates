package ru.clevertec.ecl.repository;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.util.PostgresTestContainer;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Sql("classpath:sql_script/certificate.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GiftCertificateRepositoryTest extends PostgresTestContainer {

    @Autowired
    private GiftCertificateRepository repository;

    @ParameterizedTest
    @MethodSource("getArguments")
    void findByTagNameAndPartOfNameOrDescription(String tag, String part, Long certId) {
        int size = certId == null ? 0 : 1;
        Page<GiftCertificate> page =
                repository.findByTagNameAndPartOfNameOrDescription(tag, part, Pageable.ofSize(20));

        List<GiftCertificate> content = page.getContent();

        Long actualId = content.stream()
                .findFirst()
                .map(GiftCertificate::getId)
                .orElse(null);

        assertThat(content).size().isEqualTo(size);
        assertThat(actualId).isEqualTo(certId);
    }

    static Stream<Arguments> getArguments() {
        return Stream.of(
                Arguments.of("#1", null, 1L),
                Arguments.of("#2", "i", 1L),
                Arguments.of("#3", "o", 2L),
                Arguments.of("#4", "first", null),
                Arguments.of("#5", "zero", null),
                Arguments.of("#6", "ne", 1L)
        );
    }
}
