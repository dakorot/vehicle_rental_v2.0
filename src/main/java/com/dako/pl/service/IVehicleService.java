package com.dako.pl.service;

import com.dako.pl.entities.Vehicle;

import java.util.List;

public interface IVehicleService {
    public Vehicle getVehicle(String id);
    List<Vehicle> getAllVehicles();
    List<Vehicle> getAvailableVehicles();
    Vehicle addVehicle(Vehicle vehicle);
    void removeVehicle(String vehicleId);
}
