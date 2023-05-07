package ru.clevertec.ecl.data.gift_certificate;

import ru.clevertec.ecl.data.tag.RequestTagDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record RequestGiftCertificateDto(

        @NotNull
        @Size(max = 30, min = 5)
        @Pattern(regexp = "[\\p{Alpha}\\s\\p{Punct}]+")
        String name,

        @DecimalMin("0")
        BigDecimal price,

        @NotBlank
        String description,

        @Positive
        Integer duration,

        @Valid
        List<RequestTagDto> tags
) {
}
