package ru.clevertec.ecl.data.gift_certificate;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.clevertec.ecl.data.tag.ResponseTagDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(example = """
        {
          "id": 1,
          "name": "first",
          "price": 1.11,
          "description": "one",
          "duration": 11,
          "createDate": "2023-04-01T00:00:00",
          "lastUpdateDate": "2023-05-10T10:58:58.028386",
          "tags": [
            {
              "id": 1,
              "name": "#1"
            }
          ]
        }
        """)
public record ResponseGiftCertificateDto(Long id,
                                         String name,
                                         BigDecimal price,
                                         String description,
                                         Integer duration,
                                         LocalDateTime createDate,
                                         LocalDateTime lastUpdateDate,
                                         List<ResponseTagDto> tags
) {
}
