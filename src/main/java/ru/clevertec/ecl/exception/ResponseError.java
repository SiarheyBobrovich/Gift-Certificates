package ru.clevertec.ecl.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(example = """
        {
          "errorMessage": "Requested resource not found (id = 1)",
          "errorCode": 404002
        }
        """)
public record ResponseError(String errorMessage,
                            int errorCode
) {
}
