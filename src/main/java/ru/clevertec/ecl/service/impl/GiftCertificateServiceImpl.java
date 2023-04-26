package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dao.GiftCertificateRepository;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
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
import java.util.Objects;
import java.util.Set;

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
    public List<ResponseGiftCertificateDto> findByPartOfNameOrDescription(Filter filter) {
        final List<GiftCertificate> giftCertificates = repository.findByPart(filter);

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

        final LocalDateTime currentDate = LocalDateTime.now();
        final GiftCertificate currentGiftCertificate = repository.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(id));
        final List<Tag> currentTags = currentGiftCertificate.getTags();
        final GiftCertificate updatedCertificate = mapper.requestGiftCertificateDtoToGiftCertificate(dto);
        final List<Tag> updatedTags = updatedCertificate.getTags();

        if (Objects.nonNull(updatedTags)) {
            updatedCertificate.getTags().stream()
                    .filter(tag -> currentTags.stream()
                            .map(Tag::getName)
                            .noneMatch(x -> x.equals(tag.getName())))
                    .forEach(currentTags::add);
        }

        updatedCertificate.setId(id);
        updatedCertificate.setCreateDate(currentGiftCertificate.getCreateDate());
        updatedCertificate.setLastUpdateDate(currentDate);
        updatedCertificate.setTags(currentTags);

        repository.update(updatedCertificate);
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
