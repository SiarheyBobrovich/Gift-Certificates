package ru.clevertec.ecl.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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


}
