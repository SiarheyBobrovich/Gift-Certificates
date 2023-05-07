package ru.clevertec.ecl.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.entity.GiftCertificate;

public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {

    /**
     * Find certificate by tag name, like name or description
     *
     * @param tagName           Tag name must be equal
     * @param partOfName        part of certificate name
     * @param partOfDescription part of certificate description
     * @return Page of found certificates
     */
    Page<GiftCertificate> findByTags_NameAndNameLikeOrDescriptionLike(String tagName,
                                                                      String partOfName,
                                                                      String partOfDescription,
                                                                      Pageable pageable);

    Page<GiftCertificate> findByTags_Name(String name, Pageable pageable);

    Page<GiftCertificate> findByNameLikeOrDescriptionLike(String partOfName, String partOfDescription, Pageable pageable);

    @Query("""
            SELECT c FROM GiftCertificate c LEFT JOIN c.tags t
                WHERE
                    (:tagName is null OR t.name =:tagName) AND
                        (:part is null OR
                            (c.name LIKE CONCAT('%', :part, '%') OR c.description LIKE CONCAT('%', :part, '%')))
            """)
    Page<GiftCertificate> findByTagNameAndPartOfNameOrDescription(String tagName, String part, Pageable pageable);
}
