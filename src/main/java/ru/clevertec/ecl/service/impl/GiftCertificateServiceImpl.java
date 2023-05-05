package ru.clevertec.ecl.service.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
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
import ru.clevertec.ecl.pageable.PageDto;
import ru.clevertec.ecl.pageable.Patch;
import ru.clevertec.ecl.patcher.Patcher;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.service.TagNamesService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository repository;
    private final TagNamesService tagService;
    private final GiftCertificateMapper mapper = Mappers.getMapper(GiftCertificateMapper.class);
    private final Validator validator;
    private final Patcher<GiftCertificate> patcher;

    @Override
    public ResponseGiftCertificateDto findById(Long id) {
        GiftCertificate giftCertificate = repository.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(id));
        return mapper.giftCertificateToResponseGiftCertificateDto(giftCertificate);
    }

    @Override
    public Page<ResponseGiftCertificateDto> findAll(Pageable pageable) {
        Page<GiftCertificate> giftCertificates = repository.findAll(pageable);
        return PageDto.of(giftCertificates)
                .map(mapper::giftCertificateToResponseGiftCertificateDto);
    }

    @Override
    public Page<ResponseGiftCertificateDto> findByFilter(Filter filter, Pageable pageable) {
        final Page<GiftCertificate> giftCertificates;
        String part = filter.getPart();

        if (filter.isPart() && filter.isTag()) {
            giftCertificates = repository.findByTags_NameAndNameLikeOrDescriptionLike(
                    filter.getTag(), part, part, pageable);
        } else if (filter.isPart()) {
            giftCertificates = repository.findByNameLikeOrDescriptionLike(part, part, pageable);
        } else if (filter.isTag()) {
            giftCertificates = repository.findByTags_Name(filter.getTag(), pageable);
        } else {
            giftCertificates = repository.findAll(pageable);
        }

        return PageDto.of(giftCertificates)
                .map(mapper::giftCertificateToResponseGiftCertificateDto);
    }

    @Override
    @Transactional
    public void patch(Long id, Patch patch) {
        GiftCertificate giftCertificate = repository.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(id));

        patcher.applyPatch(giftCertificate, patch);
        repository.save(giftCertificate);
    }

    @Override
    @Transactional
    public void create(RequestGiftCertificateDto dto) {
        checkDto(dto);

        GiftCertificate giftCertificate = mapper.requestGiftCertificateDtoToGiftCertificate(dto);
        List<Tag> existedTags = removeExistingTagsAndRevertThem(giftCertificate);
        giftCertificate = repository.save(giftCertificate);
        existedTags.forEach(giftCertificate::addTag);
        repository.save(giftCertificate);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void update(Long id, RequestGiftCertificateDto dto) {
        checkDto(dto);
        GiftCertificate currentCertificate = repository.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(id));

        currentCertificate.setName(dto.name());
        currentCertificate.setDescription(dto.description());
        currentCertificate.setPrice(dto.price());
        currentCertificate.setDuration(dto.duration());

        updateTags(
                currentCertificate,
                dto.tags().stream()
                        .map(RequestTagDto::name)
                        .collect(Collectors.toList()));

        repository.save(currentCertificate);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private void checkDto(RequestGiftCertificateDto dto) {
        Set<ConstraintViolation<RequestGiftCertificateDto>> validate = validator.validate(dto);
        if (!validate.isEmpty()) {
            throw new GiftCertificateValidationException(validate);
        }
    }

    private void updateTags(GiftCertificate giftCertificate, Collection<String> tagNames) {
        Set<String> currentTagNames = giftCertificate.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());
        Set<String> updatedTagNames = tagNames.stream()
                .filter(name -> !currentTagNames.contains(name))
                .collect(Collectors.toSet());
        tagService.findByNameIn(updatedTagNames).stream()
                .map(resp -> {
                    updatedTagNames.remove(resp.name());
                    return Tag.builder()
                            .id(resp.id())
                            .name(resp.name())
                            .build();
                }).forEach(giftCertificate::addTag);
        updatedTagNames.stream()
                .map(name -> Tag.builder()
                        .name(name)
                        .build())
                .forEach(giftCertificate::addTag);
    }

    private List<Tag> removeExistingTagsAndRevertThem(GiftCertificate giftCertificate) {
        List<Tag> giftCertificateTags = giftCertificate.getTags();
        Map<String, Tag> nameTagMap = giftCertificateTags.stream()
                .collect(Collectors.toMap(Tag::getName, t -> t));
        return tagService.findByNameIn(nameTagMap.keySet()).stream()
                .map(tagDto -> {
                    Tag existedTag = nameTagMap.get(tagDto.name());
                    giftCertificateTags.remove(existedTag);
                    existedTag.setId(tagDto.id());
                    return existedTag;
                }).toList();
    }
}
