package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.entity.GiftCertificate;

import java.util.List;

@Mapper(uses = TagMapper.class)
public interface GiftCertificateMapper {

    ResponseGiftCertificateDto giftCertificateToResponseGiftCertificateDto(GiftCertificate giftCertificate);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    GiftCertificate requestGiftCertificateDtoToGiftCertificate(RequestGiftCertificateDto giftCertificateDto);

    List<ResponseGiftCertificateDto> listGiftCertificateToListResponseGiftCertificateDto(List<GiftCertificate> giftCertificates);

    GiftCertificate ResponseGiftCertificateDtoToGiftCertificate(ResponseGiftCertificateDto responseGiftCertificateDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    GiftCertificate merge(@MappingTarget GiftCertificate certificate,
                          RequestGiftCertificateDto dto);
}
