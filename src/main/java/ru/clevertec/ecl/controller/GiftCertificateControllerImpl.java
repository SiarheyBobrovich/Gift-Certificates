package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.pageable.Filter;
import ru.clevertec.ecl.service.GiftCertificateService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GiftCertificateControllerImpl implements ru.clevertec.ecl.controller.api.GiftCertificateController {

    private final GiftCertificateService service;

    @Override
    public ResponseEntity<ResponseGiftCertificateDto> getByIdGiftCertificate(Long id) {
        ResponseGiftCertificateDto giftCertificateDto = service.findById(id);
        return ResponseEntity.ok().body(giftCertificateDto);
    }

    @Override
    public ResponseEntity<List<ResponseGiftCertificateDto>> getByIdGiftCertificate(
            String part,
            List<String> sort,
            String tag
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
    public ResponseEntity<List<ResponseGiftCertificateDto>> getAllGiftCertificates() {
        List<ResponseGiftCertificateDto> giftCertificateDtoList = service.findAll();
        return ResponseEntity.ok().body(giftCertificateDtoList);
    }

    @Override
    public ResponseEntity<Void> postGiftCertificate(RequestGiftCertificateDto dto) {
        service.create(dto);
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Void> putGiftCertificate(Long id,
                                                   RequestGiftCertificateDto dto) {
        service.update(id, dto);
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Void> deleteGiftCertificate(Long id) {
        service.delete(id);
        return ResponseEntity.status(204).build();
    }
}
