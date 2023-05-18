package ru.clevertec.ecl.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.clevertec.ecl.pageable.Patch;

public class PatchValidator implements ConstraintValidator<ValidPatch, Patch> {

    @Override
    public boolean isValid(Patch value, ConstraintValidatorContext context) {
        String field = value.field();
        String cValue = value.value();
        boolean result;

        if ("price".equals(field)) {
            result = cValue.matches("\\d+\\.?\\d+");
        } else if ("description".equals(field)) {
            result = cValue.startsWith("\"") && cValue.endsWith("\"") && cValue.length() <= 257;
        } else if ("duration".equals(field)) {
            result = cValue.matches("\\d+");
        } else {
            result = false;
        }

        return result;
    }
}
