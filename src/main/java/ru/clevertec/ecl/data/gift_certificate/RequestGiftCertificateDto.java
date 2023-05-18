package ru.clevertec.ecl.data.gift_certificate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.clevertec.ecl.data.tag.RequestTagDto;

import java.math.BigDecimal;
import java.util.List;

@Schema(
        example = """
                {
                        "name": "first",
                        "price": 1.11,
                        "description": "one",
                        "duration": 11,
                        "tags": [
                          {
                            "name": "#1"
                          }
                        ]
                }
                 """
)
public record RequestGiftCertificateDto(

        @NotNull
        @Size(max = 30, min = 5)
        @Pattern(regexp = "[\\p{Alpha}\\s\\p{Punct}]+")
        String name,

        @NotNull
        @DecimalMin("0")
        BigDecimal price,

        @NotBlank
        String description,

        @NotNull
        @Positive
        Integer duration,

        @Valid
        List<RequestTagDto> tags
) {
}
