package ru.clevertec.ecl.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.controller.api.GiftCertificateController;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.pageable.Filter;
import ru.clevertec.ecl.pageable.Patch;
import ru.clevertec.ecl.service.GiftCertificateService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/certificates")
@Validated
public class GiftCertificateControllerImpl implements GiftCertificateController {

    private final GiftCertificateService service;

    @Override
    @GetMapping(path = "/{id}")
    public ResponseEntity<ResponseGiftCertificateDto> getByFilter(@PathVariable Long id) {
        ResponseGiftCertificateDto giftCertificateDto = service.findById(id);
        return ResponseEntity.ok().body(giftCertificateDto);
    }

    @Override
    @GetMapping(path = "/findBy")
    public ResponseEntity<Page<ResponseGiftCertificateDto>> getByFilter(@PageableDefault(20) Pageable pageable,
                                                                        @Valid Filter filter
    ) {
        Page<ResponseGiftCertificateDto> giftCertificateDtoList = service.findByFilter(filter, pageable);

        return ResponseEntity.ok().body(giftCertificateDtoList);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<ResponseGiftCertificateDto>> getAllGiftCertificates(@PageableDefault(20) Pageable pageable) {
        Page<ResponseGiftCertificateDto> giftCertificateDtoList = service.findAll(pageable);
        return ResponseEntity.ok().body(giftCertificateDtoList);
    }

    @Override
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> postGiftCertificate(@RequestBody @Valid RequestGiftCertificateDto dto) {
        service.create(dto);
        return ResponseEntity.status(201).build();
    }

    @Override
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> putGiftCertificate(@PathVariable Long id,
                                                   @RequestBody @Valid RequestGiftCertificateDto dto) {
        service.update(id, dto);
        return ResponseEntity.status(201).build();
    }

    @Override
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteGiftCertificate(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(204).build();
    }

    @Override
    @PatchMapping(path = "/{id}")
    public ResponseEntity<Void> patchGiftCertificate(@PathVariable Long id,
                                                     @RequestBody @Valid Patch patch) {
        service.patch(id, patch);
        return ResponseEntity.status(201).build();
    }
}
