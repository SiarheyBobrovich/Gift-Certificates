package ru.clevertec.ecl.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.entity.GiftCertificate;

public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {

    @Query("""
            SELECT c FROM GiftCertificate c LEFT JOIN c.tags t
                WHERE
                    (:tagName is null OR t.name =:tagName) AND
                        (:part is null OR
                            (c.name LIKE CONCAT('%', :part, '%') OR c.description LIKE CONCAT('%', :part, '%')))
            """)
    Page<GiftCertificate> findByTagNameAndPartOfNameOrDescription(String tagName, String part, Pageable pageable);
}
