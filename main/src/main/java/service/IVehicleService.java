package service;

import entities.Vehicle;

import java.util.List;

public interface IVehicleService {
    public Vehicle getVehicle(String id);
    List<Vehicle> getAllVehicles();
    List<Vehicle> getAvailableVehicles();
    Vehicle addVehicle(Vehicle vehicle);
    void removeVehicle(String vehicleId);
}
