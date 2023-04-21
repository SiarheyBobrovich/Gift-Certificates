package ru.clevertec.ecl.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.dao.TagRepository;
import ru.clevertec.ecl.entity.Tag;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class HibernateTagRepository extends AbstractHibernateRepository<Tag, Long> implements TagRepository {

    public HibernateTagRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        Tag tag;
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Tag> criteria = builder.createQuery(Tag.class);
            Root<Tag> root = criteria.from(Tag.class);
            criteria.where(builder.equal(root.get("name"), name));
            tag = session.createQuery(criteria)
                    .getResultList().stream()
                    .findFirst()
                    .orElse(null);
            session.getTransaction().commit();
        }
        return Optional.ofNullable(tag);
    }

    @Override
    protected Class<Tag> getClassType() {
        return Tag.class;
    }
}
