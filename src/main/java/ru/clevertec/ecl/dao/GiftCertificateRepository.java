package ru.clevertec.ecl.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.entity.GiftCertificate;

public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {

    /**
     * Find a gift certificate by tag name and part of the name or description
     *
     * @param tagName  name == tag name
     * @param part     part of name or description (%part%)
     * @param pageable current page
     * @return found gift certificates page
     */
    @EntityGraph(attributePaths = "tags")
    @Query("""
            SELECT c FROM GiftCertificate c LEFT JOIN c.tags t
                WHERE
                    (?1 is null OR t.name = ?1) AND
                        (?2 is null OR
                            (c.name LIKE CONCAT('%', ?2, '%') OR c.description LIKE CONCAT('%', ?2, '%')))
            """)
    Page<GiftCertificate> findByTagNameAndPartOfNameOrDescription(String tagName,
                                                                  String part,
                                                                  Pageable pageable);
}
