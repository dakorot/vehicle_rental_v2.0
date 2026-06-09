package repository;

import entities.Vehicle;

import java.util.List;

public interface IVehicleRepository {
    public List<Vehicle> getVehicles();
    public void add(Vehicle vehicle);
    public void remove(String id);
    public Vehicle getVehicle(String id);
}
