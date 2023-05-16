package ru.clevertec.ecl.controller.open_api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.exception.ResponseError;
import ru.clevertec.ecl.pageable.Filter;
import ru.clevertec.ecl.pageable.PageDto;
import ru.clevertec.ecl.pageable.Patch;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Gift-certificate", description = "Gift-certificate API")
@OpenAPIDefinition(info = @Info(title = "Gift-Certificate API", version = "2.0.0"))
public interface GiftCertificateOpenApi {

    @Operation(
            description = "Find a gift-certificate by ID",
            tags = "Gift-certificate",
            parameters = @Parameter(name = "id", required = true, description = "Gift-certificate ID", example = "1"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseGiftCertificateDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseError.class)))
            })
    ResponseEntity<ResponseGiftCertificateDto> getByFilter(Long id);

    @Operation(
            description = "Find gift-certificates page by filter",
            tags = "Gift-certificate",
            parameters = {
                    @Parameter(name = "page", description = "Results page you want to retrieve (0..N)", example = "0"),
                    @Parameter(name = "size", description = "Number of records per page.", example = "20"),
                    @Parameter(name = "sort", description = "Sorting criteria in the format: property(,asc|desc)", example = "name,desc"),
                    @Parameter(name = "tag", description = "Tag name must be equals", example = "#1"),
                    @Parameter(name = "part", description = "Part of name or description", example = "fi"),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageDto.class)))
            })
    ResponseEntity<Page<ResponseGiftCertificateDto>> getByFilter(@Parameter(hidden = true) Pageable pageable,
                                                                 @Parameter(hidden = true) Filter filter);

    @Operation(
            description = "Find gift-certificates page",
            tags = "Gift-certificate",
            parameters = {
                    @Parameter(name = "page", description = "results page you want to retrieve (0..N)", example = "0"),
                    @Parameter(name = "size", description = "number of records per page.", example = "20"),
                    @Parameter(name = "sort", description = "sorting criteria in the format: property(,asc|desc)", example = "name,desc")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageDto.class)))
            })
    ResponseEntity<Page<ResponseGiftCertificateDto>> getAllGiftCertificates(@Parameter(hidden = true) Pageable pageable);

    @Operation(
            description = "Create a new gift-certificate",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseGiftCertificateDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseError.class)))
            }
    )
    ResponseEntity<ResponseGiftCertificateDto> postGiftCertificate(RequestGiftCertificateDto dto);

    @Operation(
            description = "Update existing gift-certificate",
            tags = "Gift-certificate",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseGiftCertificateDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseError.class)))
            }
    )
    ResponseEntity<ResponseGiftCertificateDto> putGiftCertificate(@Parameter(required = true, example = "1") Long id,
                                                                  RequestGiftCertificateDto dto);

    @Operation(
            description = "Delete existing gift-certificate",
            tags = "Gift-certificate",
            responses = @ApiResponse(responseCode = "200")
    )
    ResponseEntity<Void> deleteGiftCertificate(@Parameter(required = true, example = "1") Long id);

    @Operation(
            description = "Correction of one parameter in an existing gift-certificate",
            requestBody = @RequestBody(
                    required = true,
                    description = """
                            Field name you want to correct [description|price]
                            Price value = 123.123
                            Description value = "Some new description"
                            """,
                    content = @Content(examples = @ExampleObject("""
                            {
                                "field": "description",
                                "value": "\\"one\\""
                            }
                             """))),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseGiftCertificateDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    )
            }
    )
    ResponseEntity<ResponseGiftCertificateDto> patchGiftCertificate(@Parameter(required = true, example = "1") Long id,
                                                                    Patch patch);
}
