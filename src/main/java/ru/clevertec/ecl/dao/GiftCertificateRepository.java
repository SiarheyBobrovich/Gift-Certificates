package ru.clevertec.ecl.dao;

import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.pageable.Filter;

import java.util.List;

public interface GiftCertificateRepository extends CrudRepository<GiftCertificate, Long> {

    /**
     * Find certificate by filter
     * @param filter search options
     * @return List of found certificates or empty list
     */
    List<GiftCertificate> findByFilter(Filter filter);

}
