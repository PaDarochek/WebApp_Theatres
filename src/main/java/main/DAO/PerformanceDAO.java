package main.DAO;

import main.entities.Performance;
import main.entities.Worker;
import main.entities.WorkersPerformances;
import main.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
        List<Integer> workersIds = new ArrayList<>();
        workers.forEach(person -> {
            workersIds.add(person.getId());
        });

        cr = cb.createQuery(WorkersPerformances.class);
        root = cr.from(WorkersPerformances.class);
        cr.select(root).where(root.get("worker").in(workersIds)).where(cb.like(root.get("job"), "%director%"));
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
        actorsNames.forEach(actor -> {
            predicates[i.getAndIncrement()] = cb.like(finalRoot.get("name"), "%" + actor + "%");
        });
        cr.select(root).where(predicates);
        Query<Worker> query = session.createQuery(cr);
        List<Worker> workers = query.getResultList();
        List<Integer> workersIds = new ArrayList<>();
        workers.forEach(person -> {
            workersIds.add(person.getId());
        });

        cr = cb.createQuery(WorkersPerformances.class);
        root = cr.from(WorkersPerformances.class);
        cr.select(root).where(root.get("worker").in(workersIds)).where(cb.like(root.get("job"), "%actor%"));
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
}
