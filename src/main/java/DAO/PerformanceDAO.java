package DAO;

import entities.Performance;
import entities.Theatre;
import entities.Worker;
import entities.WorkersPerformances;
import util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PerformanceDAO implements DAO<Performance, Integer> {
    public List<Performance> getByDirector(String directorName) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cr = cb.createQuery(Worker.class);
        Root root = cr.from(Worker.class);
        cr.select(root).where(cb.like(root.get("name"), "%" + directorName + "%"));
        Query<Worker> query = session.createQuery(cr);
        List<Worker> workers = query.getResultList();
        workers.forEach(worker -> {
            System.out.println("Worker name: " + worker.getName());
        });
        List<Integer> workersIds = new ArrayList<>();
        workers.forEach(person -> {
            workersIds.add(person.getId());
        });

        cr = cb.createQuery(WorkersPerformances.class);
        root = cr.from(WorkersPerformances.class);
        Predicate[] predicates = new Predicate[2];
        predicates[0] = root.get("worker").in(workersIds);
        predicates[1] = cb.like(root.get("job"), "%director%");
        cr.select(root).where(predicates);
        Query<WorkersPerformances> query_2 = session.createQuery(cr);
        List<WorkersPerformances> workersPerformances = query_2.getResultList();
        List<Integer> performancesIds = new ArrayList<>();
        workersPerformances.forEach(entry -> {
            performancesIds.add(entry.getPerformance().getId());
        });

        cr = cb.createQuery(Performance.class);
        root = cr.from(Performance.class);
        cr.select(root).where(root.get("id").in(performancesIds));
        Query<Performance> query_3 = session.createQuery(cr);
        List<Performance> performances = query_3.getResultList();
        return performances;
    }

    public List<Performance> getByActors(List<String> actorsNames) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cr = cb.createQuery(Worker.class);
        Root root = cr.from(Worker.class);
        Predicate[] predicates = new Predicate[actorsNames.size()];
        AtomicInteger i = new AtomicInteger();
        Root finalRoot = root;
        Predicate[] finalPredicates = predicates;
        actorsNames.forEach(actor -> {
            finalPredicates[i.getAndIncrement()] = cb.like(finalRoot.get("name"), "%" + actor + "%");
        });
        cr.select(root).where(cb.or(predicates));
        Query query = session.createQuery(cr);
        List<Worker> workers = query.getResultList();
        List<Integer> workersIds = new ArrayList<>();
        workers.forEach(person -> {
            workersIds.add(person.getId());
        });

        cr = cb.createQuery(WorkersPerformances.class);
        root = cr.from(WorkersPerformances.class);
        predicates = new Predicate[2];
        predicates[0] = root.get("worker").in(workersIds);
        predicates[1] = cb.like(root.get("job"), "%actor%");
        cr.select(root).where(predicates);
        Query<WorkersPerformances> query_2 = session.createQuery(cr);
        List<WorkersPerformances> workersPerformances = query_2.getResultList();
        List<Integer> performancesIds = new ArrayList<>();
        workersPerformances.forEach(entry -> {
            performancesIds.add(entry.getPerformance().getId());
        });

        cr = cb.createQuery(Performance.class);
        root = cr.from(Performance.class);
        cr.select(root).where(root.get("id").in(performancesIds));
        Query<Performance> query_3 = session.createQuery(cr);
        List<Performance> performances = query_3.getResultList();
        return performances;
    }

    public List<Performance> getByDate(Date startDate, Date endDate) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cr = cb.createQuery(entities.Session.class);
        Root root = cr.from(entities.Session.class);
        cr.select(root).where(cb.between(root.get("date_time"), startDate, endDate));
        Query<entities.Session> query = session.createQuery(cr);
        List<entities.Session> sessions = query.getResultList();
        List<Performance> performances = new ArrayList<>();
        sessions.forEach(entry -> {
            performances.add(entry.getPerformance());
        });
        return performances;
    }

    public List<Performance> getByTheatre(String theatreName) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cr = cb.createQuery(Theatre.class);
        Root root = cr.from(Theatre.class);
        cr.select(root).where(cb.like(root.get("name"), "%" + theatreName + "%"));
        Query<Theatre> query = session.createQuery(cr);
        List<Theatre> theatres = query.getResultList();

        cr = cb.createQuery(Performance.class);
        root = cr.from(Performance.class);
        cr.select(root).where(root.get("theatre").in(theatres));
        Query<Performance> query_2 = session.createQuery(cr);
        List<Performance> performances = query_2.getResultList();
        return performances;
    }
}
