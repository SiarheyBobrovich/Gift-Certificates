package ru.clevertec.ecl.data.gift_certificate;

import ru.clevertec.ecl.data.tag.RequestTagDto;

import javax.validation.Valid;
import javax.validation.constraints.*;
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

        @Min(1)
        Integer duration,

        @Valid
        List<RequestTagDto> tags
) {
}
