package ru.clevertec.ecl.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import ru.clevertec.ecl.controller.open_api.GiftCertificateOpenApi;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.logging.Logging;
import ru.clevertec.ecl.pageable.Filter;
import ru.clevertec.ecl.pageable.Patch;
import ru.clevertec.ecl.service.GiftCertificateService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/certificates")
@Logging
public class GiftCertificateControllerImpl implements GiftCertificateOpenApi {

    private final GiftCertificateService service;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ResponseGiftCertificateDto> getById(@PathVariable Long id) {
        ResponseGiftCertificateDto giftCertificateDto = service.findById(id);

        return ResponseEntity.ok(giftCertificateDto);
    }

    @Override
    @GetMapping("/filter")
    public ResponseEntity<Page<ResponseGiftCertificateDto>> getByFilter(@PageableDefault(20) Pageable pageable,
                                                                        @Valid Filter filter) {
        Page<ResponseGiftCertificateDto> giftCertificateDtoList = service.findByFilter(filter, pageable);

        return ResponseEntity.ok(giftCertificateDtoList);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<ResponseGiftCertificateDto>> getAllGiftCertificates(@PageableDefault(20) Pageable pageable) {
        Page<ResponseGiftCertificateDto> giftCertificateDtoList = service.findAll(pageable);

        return ResponseEntity.ok(giftCertificateDtoList);
    }

    @Override
    @PostMapping
    public ResponseEntity<ResponseGiftCertificateDto> postGiftCertificate(@RequestBody @Valid RequestGiftCertificateDto dto) {
        ResponseGiftCertificateDto certificateDto = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(certificateDto);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ResponseGiftCertificateDto> putGiftCertificate(@PathVariable Long id,
                                                                         @RequestBody @Valid RequestGiftCertificateDto dto) {
        ResponseGiftCertificateDto giftCertificateDto = service.update(id, dto);

        return ResponseEntity.ok(giftCertificateDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGiftCertificate(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.ok().build();
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseGiftCertificateDto> patchGiftCertificate(@PathVariable Long id,
                                                                           @RequestBody @Valid Patch patch) {
        ResponseGiftCertificateDto certificateDto = service.patch(id, patch);

        return ResponseEntity.ok(certificateDto);
    }
}
