package ru.clevertec.ecl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.entity.GiftCertificate;

public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {

    /**
     * Find page of gift certificate by specification
     *
     * @param specification search specification
     * @param pageable      current page
     * @return found gift certificates page
     */

    Page<GiftCertificate> findAll(Specification<GiftCertificate> specification, Pageable pageable);
}
