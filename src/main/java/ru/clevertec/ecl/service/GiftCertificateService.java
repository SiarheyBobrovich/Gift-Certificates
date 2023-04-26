package ru.clevertec.ecl.service;

import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.pageable.Filter;

import java.util.List;

public interface GiftCertificateService extends CrudService<RequestGiftCertificateDto, ResponseGiftCertificateDto, Long> {

    /**
     * Find certificate by filter, map it to dto and return
     *
     * @param filter search options
     * @return List of found certificates as dto
     */
    List<ResponseGiftCertificateDto> findByFilter(Filter filter);

}
