package ru.clevertec.ecl.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.dao.TagRepository;
import ru.clevertec.ecl.entity.Tag;

@Repository
public class HibernateTagRepository extends AbstractHibernateRepository<Tag, Long> implements TagRepository {

    public HibernateTagRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Class<Tag> getClassType() {
        return Tag.class;
    }
}
