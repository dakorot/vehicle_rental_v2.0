package com.dako.pl.repository.impl;

import com.dako.pl.config.HibernateConfig;
import com.dako.pl.entities.Vehicle;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import com.dako.pl.repository.IVehicleRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Repository
@Profile("jpa")
public class VehicleRepositoryHibernate implements IVehicleRepository {

    @Override
    public void add(Vehicle vehicle) {
        if (vehicle.getId() == null || vehicle.getId().equals("0")) {
            vehicle.setId(generateNextId());
        }

        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(vehicle);
            tx.commit();
        } catch (Exception e) {
            System.out.println("An error occurred in Hibernate (Vehicle): " + e.getMessage());
        }
    }

    @Override
    public void remove(String id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Vehicle vehicle = session.find(Vehicle.class, id);
            if (vehicle != null) {
                session.remove(vehicle);
            }
            tx.commit();
        }
    }

    @Override
    public Vehicle getVehicle(String id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.find(Vehicle.class, id);
        }
    }

    @Override
    public List<Vehicle> getVehicles() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("FROM Vehicle", Vehicle.class).list();
        }
    }

    @Override
    public void save() {}

    private String generateNextId() {
        int maxId = 0;
        for (Vehicle v : getVehicles()) {
            try {
                int currentId = Integer.parseInt(v.getId());
                if (currentId > maxId) {
                    maxId = currentId;
                }
            } catch (NumberFormatException e) {
            }
        }
        return Integer.toString(maxId + 1);
    }
}