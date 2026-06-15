package config;

import entities.Rental;
import entities.User;
import entities.Vehicle;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConfig {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                String dbUrl = System.getenv("DB_URL");
                if (dbUrl == null || dbUrl.isEmpty()) {
                    dbUrl = "jdbc:postgresql://ep-withered-dust-atqj562b.c-9.us-east-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_nR8EeN9kVLgY&sslmode=require";
                }

                configuration.setProperty("hibernate.connection.url", dbUrl);
                configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
                configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
                configuration.setProperty("hibernate.hbm2ddl.auto", "update");
                configuration.setProperty("hibernate.show_sql", "true");

                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Vehicle.class);
                configuration.addAnnotatedClass(Rental.class);

                sessionFactory = configuration.buildSessionFactory();

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("An error occurred during Hibernate initialisation");
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}