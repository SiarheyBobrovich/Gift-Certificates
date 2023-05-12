package ru.clevertec.ecl.pageable;

import ru.clevertec.ecl.validation.ValidPatch;

@ValidPatch
public record Patch(String field,
                    String value
) {
}
