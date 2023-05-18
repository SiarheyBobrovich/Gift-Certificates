package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.pageable.Filter;
import ru.clevertec.ecl.util.PostgresTestContainer;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@Sql("classpath:sql_script/certificate.sql")
public class GiftCertificateServiceImplIT extends PostgresTestContainer {

    @Autowired
    private GiftCertificateServiceImpl service;

    public static Stream<Arguments> getFilterAndCertificateId() {
        return Stream.of(
                Arguments.of(
                        Filter.builder().tag("#3").part("o").build(), List.of(2L)),
                Arguments.of(
                        Filter.builder().part("th").build(), List.of(3L, 4L)),
                Arguments.of(
                        Filter.builder().tag("#2").build(), List.of(1L, 2L)),
                Arguments.of(
                        Filter.builder().tag("#7").build(), List.of()),
                Arguments.of(
                        Filter.builder().part("belarus").build(), List.of()),
                Arguments.of(
                        Filter.builder().build(), List.of(1L, 2L, 3L, 4L)));
    }

    @ParameterizedTest
    @MethodSource("getFilterAndCertificateId")
    void checkFindByFilter(Filter filter, List<Long> expectedIdList) {
        Pageable pageable = Pageable.ofSize(20);

        List<Long> actual = service.findByFilter(filter, pageable)
                .map(ResponseGiftCertificateDto::id).getContent();

        assertThat(actual).isEqualTo(expectedIdList);
    }
}
