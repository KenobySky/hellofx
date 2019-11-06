package br.sky.DAO;

import javax.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Andre Lopes
 */
public class HibernateUtil {

    private static Configuration cfg;
    private static SessionFactory sessions;

    public static void initHibernate() {

        System.out.println("Initializing Hibernate.....");
        cfg = new Configuration()
                .configure("hibernate.cfg.xml")
                .setProperty("hibernate.connection.url", "jdbc:mysql://127.0.0.1:3306/myschema?zeroDateTimeBehavior=convertToNull&useTimezone=true&serverTimezone=UTC")
                .setProperty("hibernate.connection.username", "root")
                .setProperty("hibernate.connection.password", "admin");

      
        sessions = cfg.buildSessionFactory();

        System.out.println("Adding ShutdownHook...");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });

    }

    public static EntityManager getEntityManager() {
        return sessions.createEntityManager();
    }

    public static void dispose() {
        try {
            sessions.getCurrentSession().close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            sessions.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static Configuration getCfg() {
        return cfg;
    }

    public static SessionFactory getSessions() {
        return sessions;
    }

}
