package ru.clevertec.ecl.data.gift_certificate;

import org.junit.jupiter.api.Test;
import ru.clevertec.ecl.data.tag.RequestTagDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RequestGiftCertificateDtoTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void checkPrice() {
        RequestGiftCertificateDto requestGiftCertificateDto = new RequestGiftCertificateDto("Certificate", BigDecimal.valueOf(-0.0000000001), "text", 1, null);
        Set<ConstraintViolation<RequestGiftCertificateDto>> validate = validator.validate(requestGiftCertificateDto);

        assertThat(validate).hasSize(1);
    }

    @Test
    void checkDuration() {
        RequestGiftCertificateDto requestGiftCertificateDto = new RequestGiftCertificateDto("certificate", BigDecimal.valueOf(0.0000000001), "text", 0, null);
        Set<ConstraintViolation<RequestGiftCertificateDto>> validate = validator.validate(requestGiftCertificateDto);

        assertThat(validate).hasSize(1);
    }

    @Test
    void checkTags() {
        List<RequestTagDto> requestTagDtos = List.of(new RequestTagDto("1"), new RequestTagDto("#1"));
        RequestGiftCertificateDto requestGiftCertificateDto = new RequestGiftCertificateDto("certificate", BigDecimal.valueOf(0.0000000001), "text", 40, requestTagDtos);
        Set<ConstraintViolation<RequestGiftCertificateDto>> validate = validator.validate(requestGiftCertificateDto);

        assertThat(validate).hasSize(1);
    }
}
