package ru.clevertec.ecl.data.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ResponseOrderDto(Long id,
                               Long giftCertificateId,
                               BigDecimal price,
                               LocalDateTime purchase
) {
}
