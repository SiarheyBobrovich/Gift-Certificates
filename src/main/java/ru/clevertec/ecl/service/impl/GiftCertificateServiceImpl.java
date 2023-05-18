package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.data.gift_certificate.RequestGiftCertificateDto;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.data.tag.RequestTagDto;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.pageable.Filter;
import ru.clevertec.ecl.pageable.PageDto;
import ru.clevertec.ecl.pageable.Patch;
import ru.clevertec.ecl.patcher.Patcher;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.service.TagNamesService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository repository;
    private final TagNamesService tagService;
    private final GiftCertificateMapper mapper;
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
        final String tagName = filter.getTag();
        final String part = filter.getPart();
        final Specification<GiftCertificate> specification = Specification.allOf(
                Objects.nonNull(tagName) ? getSpecificationByTagName(tagName) : Specification.anyOf(),
                Objects.nonNull(part) ? getSpecificationByPartOfNameOrDescription(part) : Specification.anyOf()
        );

        final Page<GiftCertificate> giftCertificates = repository.findAll(specification, pageable);

        return PageDto.of(giftCertificates)
                .map(mapper::giftCertificateToResponseGiftCertificateDto);
    }

    @Override
    @Transactional
    public ResponseGiftCertificateDto patch(Long id, Patch patch) {
        GiftCertificate giftCertificate = repository.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(id));

        patcher.applyPatch(giftCertificate, patch);
        GiftCertificate savedCertificate = repository.save(giftCertificate);

        return mapper.giftCertificateToResponseGiftCertificateDto(savedCertificate);
    }

    @Override
    @Transactional
    public ResponseGiftCertificateDto create(RequestGiftCertificateDto dto) {
        GiftCertificate giftCertificate = mapper.requestGiftCertificateDtoToGiftCertificate(dto);
        List<Tag> existedTags = removeExistingTagsAndRevertThem(giftCertificate);
        giftCertificate = repository.save(giftCertificate);
        existedTags.forEach(giftCertificate::addTag);
        GiftCertificate savedCertificate = repository.save(giftCertificate);

        return mapper.giftCertificateToResponseGiftCertificateDto(savedCertificate);
    }

    @Override
    @Transactional
    public ResponseGiftCertificateDto update(Long id, RequestGiftCertificateDto dto) {
        GiftCertificate currentCertificate = repository.findById(id)
                .map(certificate -> mapper.merge(certificate, dto))
                .orElseThrow(() -> new GiftCertificateNotFoundException(id));

        updateTags(
                currentCertificate,
                dto.tags().stream()
                        .map(RequestTagDto::name)
                        .collect(Collectors.toList()));

        GiftCertificate savedCertificate = repository.saveAndFlush(currentCertificate);

        return mapper.giftCertificateToResponseGiftCertificateDto(savedCertificate);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
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

    private Specification<GiftCertificate> getSpecificationByPartOfNameOrDescription(String part) {
        String likePart = String.format("%%%s%%", part);
        return Specification.anyOf(
                (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), likePart),
                (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("description"), likePart));
    }

    private Specification<GiftCertificate> getSpecificationByTagName(String tagName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tags").get("name"), tagName);
    }
}
