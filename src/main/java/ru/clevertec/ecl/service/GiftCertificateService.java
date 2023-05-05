package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.pageable.Filter;
import ru.clevertec.ecl.pageable.Patch;

public interface GiftCertificateService extends CrudService<RequestGiftCertificateDto, ResponseGiftCertificateDto, Long> {

    /**
     * Find certificate by filter, map it to dto and return
     *
     * @param filter search options
     * @return List of found certificates as dto
     */
    Page<ResponseGiftCertificateDto> findByFilter(Filter filter, Pageable pageable);

    /**
     * Patch existed certificate
     *
     * @param id    certificate ID
     * @param patch Field-value dto
     */
    void patch(Long id, Patch patch);
}
