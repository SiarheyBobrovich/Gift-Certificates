package ru.clevertec.ecl.controller.open_api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;
import ru.clevertec.ecl.exception.ResponseError;
import ru.clevertec.ecl.pageable.PageDto;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Tag", description = "Tags API")
public interface TagOpenApi {

    @Operation(
            tags = "Tag",
            description = "Find a tag by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseTagDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)))
            }
    )
    ResponseEntity<ResponseTagDto> getByIdTag(@Parameter(required = true, example = "1") Long id);

    @Operation(
            description = "Find tags page",
            tags = "Tag",
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
    ResponseEntity<Page<ResponseTagDto>> getAllTags(Pageable pageable);

    @Operation(
            tags = "Tag",
            description = "Create a new Tag",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseTagDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(allOf = ResponseError.class),
                                    examples = @ExampleObject("""
                                            {
                                                "errorMessage": "name = must match ^\\\\w+",
                                                "errorCode": 400
                                            }
                                            """)))
            }
    )
    ResponseEntity<ResponseTagDto> postTag(RequestTagDto dto);

    @Operation(
            description = "Update existing tag",
            tags = "Tag",
            operationId = "1",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseTagDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            allOf = ResponseError.class,
                                            example = """
                                                    {
                                                        "errorMessage": "name = must match ^\\\\w+",
                                                        "errorCode": 400
                                                    }
                                                    """)))
            }
    )
    ResponseEntity<ResponseTagDto> putTag(@Parameter(required = true, example = "1") Long id,
                                          RequestTagDto dto);

    @Operation(
            description = "Delete existing tag by ID",
            tags = "Tag",
            responses = @ApiResponse(responseCode = "200")
    )
    ResponseEntity<Void> deleteTag(@Parameter(required = true, example = "1") Long id);

    @Operation(
            description = "Find the most used Best Buyer Tag",
            tags = "Tag",
            responses = @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTagDto.class)))
    )
    ResponseEntity<ResponseTagDto> getMostWidelyTag();
}
