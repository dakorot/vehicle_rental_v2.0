package repository.impl;

import config.HibernateConfig;
import entities.User;
import repository.IUserRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserRepositoryHibernate implements IUserRepository {

    @Override
    public void add(User user) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
        } catch (Exception e) {
            System.out.println("An error occurred in Hibernate (User): " + e.getMessage());
        }
    }

    @Override
    public void update(User user) {
        add(user);
    }

    @Override
    public void remove(String login) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            User user = session.find(User.class, login);
            if (user != null) {
                session.remove(user);
            }
            tx.commit();
        }
    }

    @Override
    public User getUser(String login) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.find(User.class, login);
        }
    }

    @Override
    public List<User> getUsers() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    @Override
    public void save() {}
}