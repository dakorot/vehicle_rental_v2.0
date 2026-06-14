package repository;

import entities.Vehicle;

import java.util.List;

public interface IVehicleRepository {
    List<Vehicle> getVehicles();
    void add(Vehicle vehicle);
    void remove(String id);
    Vehicle getVehicle(String id);
    void save();
}
