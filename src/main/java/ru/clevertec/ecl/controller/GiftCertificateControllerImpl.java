package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.pageable.Filter;
import ru.clevertec.ecl.service.GiftCertificateService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/certificates")
public class GiftCertificateControllerImpl implements ru.clevertec.ecl.controller.api.GiftCertificateController {

    private final GiftCertificateService service;

    @Override
    @GetMapping(path = "/{id}")
    public ResponseEntity<ResponseGiftCertificateDto> getByIdGiftCertificate(@PathVariable Long id) {
        ResponseGiftCertificateDto giftCertificateDto = service.findById(id);
        return ResponseEntity.ok().body(giftCertificateDto);
    }

    @Override
    @GetMapping(path = "/findBy")
    public ResponseEntity<List<ResponseGiftCertificateDto>> getByIdGiftCertificate(
            @RequestParam(required = false) String part,
            @RequestParam(required = false) List<String> sort,
            @RequestParam(required = false) String tag
    ) {
        Filter build = Filter.builder()
                .tagName(tag)
                .partOfNameOrDescription(part)
                .build();
        build.addSortFieldName(sort);

        List<ResponseGiftCertificateDto> giftCertificateDtoList = service.findByFilter(build);

        return ResponseEntity.ok().body(giftCertificateDtoList);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ResponseGiftCertificateDto>> getAllGiftCertificates() {
        List<ResponseGiftCertificateDto> giftCertificateDtoList = service.findAll();
        return ResponseEntity.ok().body(giftCertificateDtoList);
    }

    @Override
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> postGiftCertificate(@RequestBody RequestGiftCertificateDto dto) {
        service.create(dto);
        return ResponseEntity.status(201).build();
    }

    @Override
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> putGiftCertificate(@PathVariable Long id,
                                                   @RequestBody RequestGiftCertificateDto dto) {
        service.update(id, dto);
        return ResponseEntity.status(201).build();
    }

    @Override
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteGiftCertificate(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(204).build();
    }
}
