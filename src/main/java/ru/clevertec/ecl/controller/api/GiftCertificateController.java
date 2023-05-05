package ru.clevertec.ecl.controller.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.pageable.Filter;
import ru.clevertec.ecl.pageable.Patch;

public interface GiftCertificateController {

    ResponseEntity<ResponseGiftCertificateDto> getByFilter(Long id);

    ResponseEntity<Page<ResponseGiftCertificateDto>> getByFilter(Pageable pageable,
                                                                 Filter filter
    );

    ResponseEntity<Page<ResponseGiftCertificateDto>> getAllGiftCertificates(Pageable pageable);

    ResponseEntity<Void> postGiftCertificate(RequestGiftCertificateDto dto);

    ResponseEntity<Void> putGiftCertificate(Long id,
                                            RequestGiftCertificateDto dto);

    ResponseEntity<Void> deleteGiftCertificate(Long id);

    ResponseEntity<Void> patchGiftCertificate(Long id,
                                              Patch patch);
}
