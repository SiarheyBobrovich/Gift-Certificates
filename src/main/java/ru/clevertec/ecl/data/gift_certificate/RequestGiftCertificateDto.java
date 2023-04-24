package ru.clevertec.ecl.data.gift_certificate;

import ru.clevertec.ecl.data.tag.RequestTagDto;

import java.math.BigDecimal;
import java.util.List;

public record RequestGiftCertificateDto(
        String name,
        BigDecimal price,
        String description,
        Integer duration,
        List<RequestTagDto> tags
) {
}
