package util;

import entities.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory = null;
    static {
        try {
            sessionFactory = new Configuration().configure()
                    .addAnnotatedClass(Theatre.class)
                    .addAnnotatedClass(Performance.class)
                    .addAnnotatedClass(Session.class)
                    .addAnnotatedClass(Ticket.class)
                    .addAnnotatedClass(Worker.class)
                    .addAnnotatedClass(Admin.class)
                    .addAnnotatedClass(WorkersPerformances.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
