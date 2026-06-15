package service.impl;

import entities.Rental;
import entities.Vehicle;
import repository.IVehicleRepository;
import repository.impl.VehicleRepositoryImpl;
import service.IVehicleService;
import service.VehicleValidator;

import java.util.ArrayList;
import java.util.List;

public class VehicleServiceImpl implements IVehicleService {
    private final IVehicleRepository vehicleRepo;
    private final RentalServiceImpl rentalServiceImpl;
    private final VehicleValidator vehicleValidator;

    public VehicleServiceImpl(IVehicleRepository vehicleRepo, RentalServiceImpl rentalServiceImpl, VehicleValidator vehicleValidator) {
        this.vehicleRepo = vehicleRepo;
        this.rentalServiceImpl = rentalServiceImpl;
        this.vehicleValidator = vehicleValidator;
    }

    public Vehicle getVehicle(String id) {
        for (Vehicle v : vehicleRepo.getVehicles()) {
            if (id.equals(v.getId()))
                return v;
        }
        return null;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepo.getVehicles();
    }

    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> allVehicles = vehicleRepo.getVehicles();
        List<Rental> allRentals = rentalServiceImpl.getAllRentals();
        List<Vehicle> availableVehicles = new ArrayList<>();

        for (Vehicle vehicle : allVehicles) {
            boolean isRented = false;
            for (Rental rental : allRentals) {
                if (rental.vehicle.getId().equals(vehicle.getId())) {
                    isRented = true;
                    break;
                }
            }

            if (!isRented) {
                availableVehicles.add(vehicle);
            }
        }
        return availableVehicles;
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        vehicleValidator.validate(vehicle);
        vehicleRepo.add(vehicle);

        if (vehicleRepo instanceof VehicleRepositoryImpl) {
            ((VehicleRepositoryImpl) vehicleRepo).save();
        }

        return vehicle;
    }

    public void removeVehicle(String vehicleId) {
        boolean isRented = false;
        for (Rental rental : rentalServiceImpl.getAllRentals()) {
            if (rental.vehicle.getId().equals(vehicleId)) {
                isRented = true;
                break;
            }
        }

        if (isRented) {
            throw new IllegalStateException("Unable to remove a vehicle because it's currently rented.");
        }

        vehicleRepo.remove(vehicleId);

        if (vehicleRepo instanceof VehicleRepositoryImpl) {
            ((VehicleRepositoryImpl) vehicleRepo).save();
        }
    }
}
