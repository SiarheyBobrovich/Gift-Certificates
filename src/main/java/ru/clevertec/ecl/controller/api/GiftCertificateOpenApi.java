package ru.clevertec.ecl.controller.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.pageable.Filter;
import ru.clevertec.ecl.pageable.Patch;

public interface GiftCertificateOpenApi {

    ResponseEntity<ResponseGiftCertificateDto> getByFilter(Long id);

    ResponseEntity<Page<ResponseGiftCertificateDto>> getByFilter(Pageable pageable,
                                                                 Filter filter
    );

    ResponseEntity<Page<ResponseGiftCertificateDto>> getAllGiftCertificates(Pageable pageable);

    ResponseEntity<ResponseGiftCertificateDto> postGiftCertificate(RequestGiftCertificateDto dto);

    ResponseEntity<ResponseGiftCertificateDto> putGiftCertificate(Long id,
                                                                  RequestGiftCertificateDto dto);

    ResponseEntity<Void> deleteGiftCertificate(Long id);

    ResponseEntity<ResponseGiftCertificateDto> patchGiftCertificate(Long id,
                                                                    Patch patch);
}
