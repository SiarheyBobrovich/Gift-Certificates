package ru.clevertec.ecl.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/api/v1/certificates")
public interface GiftCertificateController {

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<ResponseGiftCertificateDto> getByIdGiftCertificate(@PathVariable Long id);

    @GetMapping(path = "/findBy", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<ResponseGiftCertificateDto>> getByIdGiftCertificate(
            @RequestParam(required = false) String part,
            @RequestParam(required = false) List<String> sort,
            @RequestParam(required = false) String tag
    );

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<ResponseGiftCertificateDto>> getAllGiftCertificates();

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<Void> postGiftCertificate(@RequestBody RequestGiftCertificateDto dto);

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<Void> putGiftCertificate(@PathVariable Long id,
                                            @RequestBody RequestGiftCertificateDto dto);

    @DeleteMapping(path = "/{id}")
    ResponseEntity<Void> deleteGiftCertificate(@PathVariable Long id);
}
