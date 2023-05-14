package ru.clevertec.ecl.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.controller.api.GiftCertificateOpenApi;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.pageable.Filter;
import ru.clevertec.ecl.pageable.Patch;
import ru.clevertec.ecl.service.GiftCertificateService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/certificates")
public class GiftCertificateControllerImpl implements GiftCertificateOpenApi {

    private final GiftCertificateService service;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ResponseGiftCertificateDto> getByFilter(@PathVariable Long id) {
        ResponseGiftCertificateDto giftCertificateDto = service.findById(id);

        log.info("GET/{}\nResponse::{}", id, giftCertificateDto);
        return ResponseEntity.ok(giftCertificateDto);
    }

    @Override
    @GetMapping("/filter")
    public ResponseEntity<Page<ResponseGiftCertificateDto>> getByFilter(@PageableDefault(20) Pageable pageable,
                                                                        @Valid Filter filter) {
        Page<ResponseGiftCertificateDto> giftCertificateDtoList = service.findByFilter(filter, pageable);
        log.info("GET/filter::{}\nResponse::{}", filter, giftCertificateDtoList);

        return ResponseEntity.ok(giftCertificateDtoList);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<ResponseGiftCertificateDto>> getAllGiftCertificates(@PageableDefault(20) Pageable pageable) {
        Page<ResponseGiftCertificateDto> giftCertificateDtoList = service.findAll(pageable);
        log.info("GET::{}\nResponse::{}", pageable, giftCertificateDtoList);

        return ResponseEntity.ok(giftCertificateDtoList);
    }

    @Override
    @PostMapping
    public ResponseEntity<ResponseGiftCertificateDto> postGiftCertificate(@RequestBody @Valid RequestGiftCertificateDto dto) {
        ResponseGiftCertificateDto certificateDto = service.create(dto);
        log.info("POST::{}\nResponse::{}", dto, certificateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(certificateDto);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ResponseGiftCertificateDto> putGiftCertificate(@PathVariable Long id,
                                                                         @RequestBody @Valid RequestGiftCertificateDto dto) {
        ResponseGiftCertificateDto giftCertificateDto = service.update(id, dto);
        log.info("PUT/{}::{}\nResponse::{}", id, dto, giftCertificateDto);

        return ResponseEntity.ok(giftCertificateDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGiftCertificate(@PathVariable Long id) {
        service.delete(id);
        log.info("DELETE/{}", id);

        return ResponseEntity.ok().build();
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseGiftCertificateDto> patchGiftCertificate(@PathVariable Long id,
                                                                           @RequestBody @Valid Patch patch) {
        ResponseGiftCertificateDto certificateDto = service.patch(id, patch);
        log.info("PATCH/{}::{}\nResponse::{}", id, patch, certificateDto);

        return ResponseEntity.ok(certificateDto);
    }
}
