package ru.clevertec.ecl.dao;

import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.pageable.Filter;

import java.util.List;

public interface GiftCertificateRepository extends CrudRepository<GiftCertificate, Long> {

    List<GiftCertificate> findByPart(Filter filter);

    GiftCertificate loadTags(GiftCertificate giftCertificate);
}
