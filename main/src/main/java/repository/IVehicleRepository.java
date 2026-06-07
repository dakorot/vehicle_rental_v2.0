package repository;

import entities.Vehicle;

import java.util.List;

public interface IVehicleRepository {
    public void rentVehicle(Vehicle vehicle);
    public Vehicle returnVehicle(String id);
    public List<Vehicle> getVehicles();
    public void save();
    public void load();
    public void add(Vehicle vehicle);
    public void remove(String id);
    public Vehicle getVehicle(String id);
}
