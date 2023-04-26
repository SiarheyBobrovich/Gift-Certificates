package ru.clevertec.ecl.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.dao.TagRepository;
import ru.clevertec.ecl.entity.Tag;

import java.util.Optional;

@Repository
public class HibernateTagRepository extends AbstractHibernateRepository<Tag, Long> implements TagRepository {

    public HibernateTagRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<Tag> findTagByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        return session.createNamedQuery("tagByName", Tag.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst();
    }

    @Override
    protected Class<Tag> getClassType() {
        return Tag.class;
    }
}
