package main.DAO;

import main.util.HibernateUtil;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public interface DAO<E, K> {
    default boolean create(E entity) {
        if (Objects.isNull(entity)) {
            return false;
        }
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();
        return true;
    }

    default E update(E entity) {
        if (Objects.isNull(entity)) {
            return entity;
        }
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(entity);
        transaction.commit();
        session.close();
        return entity;
    }

    default boolean delete(E entity) {
        if (Objects.isNull(entity)) {
            return false;
        }
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(entity);
        transaction.commit();
        session.close();
        return true;
    }

    default List<E> getAll(Class persistentClass, int firstResult, int maxResults) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("from" + persistentClass.getName())
                .setFirstResult(firstResult).setMaxResults(maxResults);
        List<E> result = query.list();
        session.close();
        return result;
    }

    default List<E> getAll(Class persistentClass) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("from" + persistentClass.getName());
        List<E> result = query.list();
        session.close();
        return result;
    }

    default E getEntityById(int id, Class persistentClass) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        E result = (E) session.get(persistentClass, id);
        session.close();
        return result;
    }

    default Long getCount(Class persistentClass) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cr = cb.createQuery(Long.class);
        Root root = cr.from(persistentClass);
        cr.select(cb.count(root));
        Query<Long> query = session.createQuery(cr);
        List<Long> result = query.getResultList();
        session.close();
        return result.get(0);
    }

    default List<E> filter(Map<String, List> filters, Class persistentClass, int firstResult, int maxResults) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        filters.entrySet().forEach(entry -> {
            String filterName = entry.getKey();
            List filterParams = entry.getValue();
            Filter filter = session.enableFilter(filterName);
            Set<String> paramNames = filter.getFilterDefinition().getParameterNames();
            AtomicInteger i = new AtomicInteger();
            paramNames.forEach(paramName -> {
                filter.setParameter(paramName, filterParams.get(i.getAndIncrement()));
            });
        });
        Query query = session.createQuery("from" + persistentClass.getName())
                .setFirstResult(firstResult).setMaxResults(maxResults);
        List<E> result = query.list();
        session.close();
        return result;
    }

    default List<E> filter(Map<String, List> filters, Class persistentClass) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        filters.entrySet().forEach(entry -> {
            String filterName = entry.getKey();
            List filterParams = entry.getValue();
            Filter filter = session.enableFilter(filterName);
            Set<String> paramNames = filter.getFilterDefinition().getParameterNames();
            AtomicInteger i = new AtomicInteger();
            paramNames.forEach(paramName -> {
                filter.setParameter(paramName, filterParams.get(i.getAndIncrement()));
            });
        });
        Query query = session.createQuery("from" + persistentClass.getName());
        List<E> result = query.list();
        session.close();
        return result;
    }

    default List<E> sort(Map<String, String> orders, Class persistentClass) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cr = cb.createQuery(persistentClass);
        Root root = cr.from(persistentClass);
        List<Order> orderList = new ArrayList<>();
        orders.forEach((key, value) -> {
            orderList.add(value == "asc" ? Order.asc(key) : Order.desc(key));
        });
        cr.orderBy(orderList);
        cr.select(root);
        Query query = session.createQuery(cr);
        List result = query.getResultList();
        session.close();
        return result;
    }
}
