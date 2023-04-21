package ru.clevertec.ecl.dao.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.clevertec.ecl.dao.CrudRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractHibernateRepository<T, ID extends Serializable> implements CrudRepository<T, ID> {

    protected final SessionFactory sessionFactory;

    @Override
    public T save(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.save(entity);
            session.getTransaction().commit();
        }
        return entity;
    }

    @Override
    public T update(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.update(entity);
            session.getTransaction().commit();
        }
        return entity;
    }

    @Override
    public void delete(ID id) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.byId(getClassType())
                    .loadOptional(id)
                    .ifPresent(session::delete);
            session.getTransaction().commit();
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        Optional<T> entity;
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            entity = session.byId(getClassType())
                    .loadOptional(id);
            session.getTransaction().commit();
        }
        return entity;
    }

    @Override
    public List<T> findAll() {
        List<T> entity;
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteria = builder.createQuery(getClassType());
            criteria.from(getClassType());
            entity = session.createQuery(criteria).getResultList();
            session.getTransaction().commit();
        }
        return entity;
    }

    protected abstract Class<T> getClassType();
}
