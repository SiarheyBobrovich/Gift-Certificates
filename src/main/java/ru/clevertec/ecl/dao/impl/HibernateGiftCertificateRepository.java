package ru.clevertec.ecl.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.dao.GiftCertificateRepository;
import ru.clevertec.ecl.dao.TagRepository;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;

@Repository
public class HibernateGiftCertificateRepository extends AbstractHibernateRepository<GiftCertificate, Long> implements GiftCertificateRepository {

    public HibernateGiftCertificateRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Class<GiftCertificate> getClassType() {
        return GiftCertificate.class;
    }
}
