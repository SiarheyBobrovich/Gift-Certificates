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
import ru.clevertec.ecl.data.order.CreateOrderDto;
import ru.clevertec.ecl.data.order.ResponseOrderDto;
import ru.clevertec.ecl.exception.ResponseError;
import ru.clevertec.ecl.pageable.PageDto;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Order", description = "Order API")
public interface OrderOpenApi {

    @Operation(
            tags = "Order",
            description = "Find all orders by user ID",
            parameters = {
                    @Parameter(name = "page", description = "results page you want to retrieve (0..N)", example = "0"),
                    @Parameter(name = "size", description = "number of records per page.", example = "20"),
                    @Parameter(name = "sort", description = "sorting criteria in the format: property(,asc|desc)", example = "id,desc")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageDto.class))),
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
    ResponseEntity<Page<ResponseOrderDto>> findAllOrdersByUserId(@Parameter(hidden = true) Pageable pageable,
                                                                 @Parameter(required = true, example = "1") Long userId);

    @Operation(
            description = "Create a new order",
            tags = "Order",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(allOf = ResponseOrderDto.class, example = """
                                            {
                                              "id": 18,
                                              "giftCertificateId": 1,
                                              "price": 1.11,
                                              "purchase": "2023-05-17T02:03:35.016091514"
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
    ResponseEntity<ResponseOrderDto> createNewOrder(CreateOrderDto createOrderDto);
}
