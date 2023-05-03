package ru.clevertec.ecl.controller.api;

import org.springframework.http.ResponseEntity;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;

import java.util.List;

public interface GiftCertificateController {

    ResponseEntity<ResponseGiftCertificateDto> getByIdGiftCertificate(Long id);

    ResponseEntity<List<ResponseGiftCertificateDto>> getByIdGiftCertificate(
            String part,
            List<String> sort,
            String tag
    );

    ResponseEntity<List<ResponseGiftCertificateDto>> getAllGiftCertificates();

    ResponseEntity<Void> postGiftCertificate(RequestGiftCertificateDto dto);

    ResponseEntity<Void> putGiftCertificate(Long id,
                                            RequestGiftCertificateDto dto);

    ResponseEntity<Void> deleteGiftCertificate(Long id);
}
