package ru.clevertec.ecl.data.gift_certificate;

import ru.clevertec.ecl.data.tag.ResponseTagDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ResponseGiftCertificateDto(
        Long id,
        String name,
        BigDecimal price,
        String description,
        Integer duration,
        LocalDateTime createDate,
        LocalDateTime lastUpdateDate,
        List<ResponseTagDto> tags

) {
}
