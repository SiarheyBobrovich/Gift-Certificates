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

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class HibernateGiftCertificateRepository extends AbstractHibernateRepository<GiftCertificate, Long> implements GiftCertificateRepository {

    public HibernateGiftCertificateRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<GiftCertificate> findByFilter(Filter filter) {
        String tagName = filter.getTagName();
        String part = filter.getPartOfNameOrDescription();
        Map<String, Order> fieldNameOrder = filter.getSortFieldName();
        String partLike = String.format("%%%s%%", part);
        boolean isTagName = Objects.nonNull(tagName);
        boolean isPart = Objects.nonNull(part);
        boolean isOrder = !fieldNameOrder.isEmpty();

        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();

        CriteriaQuery<GiftCertificate> certQuery = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> gcRoot = certQuery.from(GiftCertificate.class);
        ListJoin<Object, Object> tagsJoin = gcRoot.joinList("tags", JoinType.LEFT);

        Predicate nameOrDescriptionPredicate = builder.or(
                builder.like(gcRoot.get("name"), partLike),
                builder.like(gcRoot.get("description"), partLike));
        Predicate tagNamePredicate = builder.equal(tagsJoin.get("name"), tagName);

        //Where section
        certQuery = isTagName && isPart ? certQuery.where(builder.and(tagNamePredicate, nameOrDescriptionPredicate)) :
                isTagName ? certQuery.where(tagNamePredicate) :
                        isPart ? certQuery.where(nameOrDescriptionPredicate) :
                                certQuery;
        certQuery = isOrder ? certQuery.orderBy(fieldNameOrder.entrySet().stream()
                .map(fieldOrder -> {
                    String fieldName = fieldOrder.getKey();
                    Order order = fieldOrder.getValue();
                    return Order.ASC.equals(order) ?
                            builder.asc(gcRoot.get(fieldName)) : builder.desc(gcRoot.get(fieldName));
                }).toList()) : certQuery;

        certQuery.select(gcRoot);

        return session.createQuery(certQuery).getResultList();
    }

    @Override
    public GiftCertificate update(GiftCertificate entity) {
        Session session = sessionFactory.getCurrentSession();
        Query<Tag> query = session.createNamedQuery("tagByName", Tag.class);
        List<Tag> tags = entity.getTags().stream().toList();
        session.detach(entity);
        tags.forEach(tag -> {
            if (Objects.nonNull(tag.getId())) {
                return;
            }
            query.setParameter("name", tag.getName())
                    .getResultStream()
                    .findFirst()
                    .ifPresent(cTag -> tag.setId(cTag.getId()));
        });
        session.merge(entity);
        return entity;
    }

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        Session session = sessionFactory.getCurrentSession();
        loadTags(giftCertificate);
        session.persist(giftCertificate);

        return giftCertificate;
    }

    private void loadTags(GiftCertificate giftCertificate) {
        Session session = sessionFactory.getCurrentSession();
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
    protected Class<GiftCertificate> getClassType() {
        return GiftCertificate.class;
    }
}
