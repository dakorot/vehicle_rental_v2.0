package repository.impl;

import config.HibernateConfig;
import entities.Rental;
import repository.IRentalRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class RentalRepositoryHibernate implements IRentalRepository {

    @Override
    public void add(Rental rental) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            if (rental.id == null || rental.id.equals("0")) {
                rental.id = generateNextId(session);
            }

            session.merge(rental);
            tx.commit();
        } catch (Exception e) {
            System.out.println("An error occurred in Hibernate (Rental): " + e.getMessage());
        }
    }

    @Override
    public void remove(String rentalId) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Rental rental = session.find(Rental.class, rentalId);
            if (rental != null) {
                session.remove(rental);
            }

            tx.commit();
        } catch (Exception e) {
            System.out.println("An error occurred during removal in Hibernate (Rental): " + e.getMessage());
        }
    }

    @Override
    public List<Rental> getRentals() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("FROM Rental", Rental.class).list();
        }
    }

    @Override
    public void save() {
    }

    @Override
    public void load() {
    }

    private String generateNextId(Session session) {
        List<String> ids = session.createQuery("SELECT r.id FROM Rental r", String.class).list();
        int maxId = 0;
        for (String idStr : ids) {
            try {
                int currentId = Integer.parseInt(idStr);
                if (currentId > maxId) {
                    maxId = currentId;
                }
            } catch (NumberFormatException e) {
            }
        }
        return Integer.toString(maxId + 1);
    }
}