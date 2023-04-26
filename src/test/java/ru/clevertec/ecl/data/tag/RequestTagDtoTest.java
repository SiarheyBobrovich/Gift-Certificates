package ru.clevertec.ecl.data.tag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RequestTagDtoTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void checkNameNull() {
        RequestTagDto tagDto = new RequestTagDto(null);
        Set<ConstraintViolation<RequestTagDto>> validate = validator.validate(tagDto);

        assertThat(validate).isNotEmpty();
    }

    @Test
    void checkNameEmpty() {
        RequestTagDto tagDto = new RequestTagDto("");
        Set<ConstraintViolation<RequestTagDto>> validate = validator.validate(tagDto);

        assertThat(validate).isNotEmpty();
    }

    @Test
    @DisplayName("Not start with #")
    void checkName() {
        RequestTagDto tagDto = new RequestTagDto("start");
        Set<ConstraintViolation<RequestTagDto>> validate = validator.validate(tagDto);

        assertThat(validate).isNotEmpty();
    }

    @Test
    void checkNameLength20() {
        RequestTagDto tagDto = new RequestTagDto("#dadfjufddakndasdfew");
        Set<ConstraintViolation<RequestTagDto>> validate = validator.validate(tagDto);

        assertThat(validate).isEmpty();
    }

    @Test
    void checkNameLength21() {
        RequestTagDto tagDto = new RequestTagDto("#dadfjufddakndasdfewd");
        Set<ConstraintViolation<RequestTagDto>> validate = validator.validate(tagDto);

        assertThat(validate).isNotEmpty();
    }
}