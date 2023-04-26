package ru.clevertec.ecl.dao.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
        Session session = sessionFactory.getCurrentSession();
        session.persist(entity);
        session.flush();
        return entity;
    }

    @Override
    public T update(T entity) {
        Session session = sessionFactory.getCurrentSession();
        session.update(entity);
        session.flush();
        return entity;
    }

    @Override
    public void delete(ID id) {
        Session session = sessionFactory.getCurrentSession();
        session.byId(getClassType())
                .loadOptional(id)
                .ifPresent(session::delete);
        session.flush();
    }

    @Override
    public Optional<T> findById(ID id) {
        Session session = sessionFactory.getCurrentSession();
        return session.byId(getClassType()).loadOptional(id);
    }

    @Override
    public List<T> findAll() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(getClassType());
        criteria.from(getClassType());
        return session.createQuery(criteria).getResultList();
    }

    protected abstract Class<T> getClassType();
}
