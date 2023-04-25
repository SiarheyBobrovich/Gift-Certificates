package ru.clevertec.ecl.util;

import lombok.Builder;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Builder
public class CertificateBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private BigDecimal price = BigDecimal.valueOf(1);

    @Builder.Default
    private String name = "Certificate";

    @Builder.Default
    private String description = "description";

    @Builder.Default
    private Integer duration = 1;

    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.MIN;

    @Builder.Default
    private LocalDateTime lastUpdateDate = LocalDateTime.MIN;

    @Builder.Default
    private List<Tag> tags = List.of(new Tag(1L, "#1"), new Tag(2L, "#2"));

    public RequestGiftCertificateDto getRequestDto() {
        return new RequestGiftCertificateDto(
                name,
                price,
                description,
                duration,
                tags.stream()
                        .map(x -> new RequestTagDto(x.getName()))
                        .toList());
    }

    public GiftCertificate getEntity() {
        return GiftCertificate.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .tags(tags)
                .build();
    }

    public Optional<GiftCertificate> getOptionalEntity() {
        return Optional.of(getEntity());
    }

    public ResponseGiftCertificateDto getResponseDto() {
        return new ResponseGiftCertificateDto(
                id,
                name,
                price,
                description,
                duration,
                createDate,
                lastUpdateDate,
                tags.stream()
                        .map(x -> ResponseTagDto.builder()
                                .id(x.getId())
                                .name(x.getName())
                                .build())
                        .toList()
        );
    }
}
