package ru.clevertec.ecl.controller.open_api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import ru.clevertec.ecl.data.user.ResponseUserDto;
import ru.clevertec.ecl.exception.ResponseError;
import ru.clevertec.ecl.pageable.PageDto;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "User", description = "User API")
public interface UserOpenApi {

    @Operation(
            description = "Find users page",
            tags = "User",
            parameters = {
                    @Parameter(name = "page", description = "results page you want to retrieve (0..N)", example = "0"),
                    @Parameter(name = "size", description = "number of records per page.", example = "20"),
                    @Parameter(name = "sort", description = "sorting criteria in the format: property(,asc|desc)", example = "firstName,desc")
            },
            responses = @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PageDto.class)))
    )
    ResponseEntity<Page<ResponseUserDto>> getAllUsers(@Parameter(hidden = true) Pageable pageable);

    @Operation(
            description = "Find an user by ID",
            tags = "User",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            allOf = ResponseUserDto.class,
                                            example = """
                                                    {
                                                      "id": 1,
                                                      "firstName": "Siarhey",
                                                      "lastName": "Babrovich",
                                                      "orders": [
                                                        {
                                                          "id": 1,
                                                          "giftCertificateId": 1,
                                                          "price": 1,
                                                          "purchase": "2023-05-10T00:31:23.12256"
                                                        },
                                                        {
                                                          "id": 2,
                                                          "giftCertificateId": 1,
                                                          "price": 1,
                                                          "purchase": "2023-05-10T00:31:23.12256"
                                                        }
                                                      ]
                                                    }
                                                    """))),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(allOf = ResponseError.class, example = """
                                            {
                                                "errorMessage": "Requested resource not found (id = 1)",
                                                "errorCode": 40403
                                            }
                                            """)))
            }
    )
    ResponseEntity<ResponseUserDto> getById(@Parameter(required = true, example = "1") Long id);
}
