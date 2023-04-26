package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dao.GiftCertificateRepository;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.exception.GiftCertificateValidationException;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.pageable.Filter;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.transaction.Transaction;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transaction(readOnly = true)
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository repository;
    private final GiftCertificateMapper mapper = Mappers.getMapper(GiftCertificateMapper.class);

    private final Validator validator;

    @Override
    public ResponseGiftCertificateDto findById(Long id) {
        GiftCertificate giftCertificate = repository.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(id));
        return mapper.giftCertificateToResponseGiftCertificateDto(giftCertificate);
    }

    @Override
    public List<ResponseGiftCertificateDto> findAll() {
        List<GiftCertificate> giftCertificates = repository.findAll();

        return mapper.listGiftCertificateToListResponseGiftCertificateDto(giftCertificates);
    }

    @Override
    public List<ResponseGiftCertificateDto> findByFilter(Filter filter) {
        final List<GiftCertificate> giftCertificates = repository.findByFilter(filter);

        return mapper.listGiftCertificateToListResponseGiftCertificateDto(giftCertificates);
    }

    @Override
    @Transaction
    public void create(RequestGiftCertificateDto dto) {
        checkDto(dto);

        GiftCertificate giftCertificate = mapper.requestGiftCertificateDtoToGiftCertificate(dto);

        LocalDateTime now = LocalDateTime.now();
        giftCertificate.setCreateDate(now);
        giftCertificate.setLastUpdateDate(now);

        repository.save(giftCertificate);
    }

    @Override
    @Transaction(isolationLevel = Transaction.IsolationLevel.REPEATABLE_READ)
    public void update(Long id, RequestGiftCertificateDto dto) {
        checkDto(dto);

        GiftCertificate currentCertificate = repository.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(id));

        Set<String> tagNames = currentCertificate.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        currentCertificate.setLastUpdateDate(LocalDateTime.now());
        currentCertificate.setName(dto.name());
        currentCertificate.setDescription(dto.description());
        currentCertificate.setPrice(dto.price());
        currentCertificate.setDuration(dto.duration());

        dto.tags().stream()
                .map(RequestTagDto::name)
                .filter(name -> !tagNames.contains(name))
                .map(name -> Tag.builder()
                        .name(name)
                        .build())
                .forEach(currentCertificate::addTag);

        repository.update(currentCertificate);
    }

    @Override
    @Transaction
    public void delete(Long id) {
        repository.delete(id);
    }

    private void checkDto(RequestGiftCertificateDto dto) {
        Set<ConstraintViolation<RequestGiftCertificateDto>> validate = validator.validate(dto);
        if (!validate.isEmpty()) {
            throw new GiftCertificateValidationException(validate);
        }
    }
}
