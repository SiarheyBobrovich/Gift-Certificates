package ru.clevertec.ecl.mapper;

import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.ecl.dao.impl.HibernateGiftCertificateRepository;
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

}
