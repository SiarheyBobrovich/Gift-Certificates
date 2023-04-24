package ru.clevertec.ecl.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.dao.GiftCertificateRepository;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.pageable.Filter;
import ru.clevertec.ecl.pageable.Order;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class HibernateGiftCertificateRepository extends AbstractHibernateRepository<GiftCertificate, Long> implements GiftCertificateRepository {

    public HibernateGiftCertificateRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<GiftCertificate> findByPart(Filter filter) {
        String selectHQL = """
                SELECT DISTINCT gc FROM GiftCertificate gc
                JOIN gc.tags t
                """;
        String tagName = filter.getTagName();
        String part = filter.getPartOfNameOrDescription();
        String orderBy = getOrderBy(filter.getSortFieldName());

        String where = where(tagName, part);

        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();
            Query<GiftCertificate> query = session.createQuery(selectHQL + where + orderBy
                    , GiftCertificate.class);
            if (Objects.nonNull(part)) {
                query.setParameter("part", "%" + part + "%");
            }
            if (Objects.nonNull(tagName)) {
                query.setParameter("tagName", tagName);
            }

            List<GiftCertificate> resultList = query.getResultList();
            session.getTransaction().commit();
            return resultList;
        }
    }

    private String where(String tagName, String partOfNameOrDescription) {
        StringBuilder whereBuilder = new StringBuilder();
        if (Objects.nonNull(tagName)) {
            whereBuilder.append("""
                    t.name = :tagName
                    """);
        }
        if (Objects.nonNull(partOfNameOrDescription)) {
            whereBuilder.append(whereBuilder.isEmpty() ? "" : "AND\n")
                    .append("""
                            (gc.name LIKE :part OR
                            gc.description LIKE :part)
                            """);
        }
        return whereBuilder.isEmpty() ? "" : whereBuilder.insert(0, "WHERE ").toString();
    }

    private String getOrderBy(Map<String, Order> fields) {
        if (fields.isEmpty()) {
            return "";
        }
        return fields.entrySet().stream()
                .map(entry -> String.join(" ", entry.getKey(), entry.getValue().name()))
                .map(x -> "gc." + x)
                .reduce((x1, x2) -> String.join(", ", x1, x2))
                .map(x -> "ORDER BY " + x)
                .orElse("");
    }

    @Override
    public GiftCertificate update(GiftCertificate entity) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();

            session.merge(entity);

            session.getTransaction().commit();
        }
        return entity;
    }

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();

            loadTags(giftCertificate, session);
            session.persist(giftCertificate);

            session.getTransaction().commit();
        }
        return giftCertificate;
    }

    private void loadTags(GiftCertificate giftCertificate, Session session) {
        final List<Tag> tags = giftCertificate.getTags();
        if (Objects.nonNull(tags)) {
            Query<Tag> query = session.createNamedQuery("tagByName", Tag.class);
            List<Tag> currentTags = tags.stream()
                    .map(tag -> query.setParameter("name", tag.getName())
                            .getResultStream()
                            .findFirst()
                            .orElse(tag))
                    .toList();
            giftCertificate.setTags(currentTags);
        }
    }

    @Override
    public GiftCertificate loadTags(GiftCertificate giftCertificate) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();
            session.load(giftCertificate, giftCertificate.getId());
            giftCertificate.getTags().toString();
            session.getTransaction().commit();
        }
        return giftCertificate;
    }

    @Override
    protected Class<GiftCertificate> getClassType() {
        return GiftCertificate.class;
    }
}
