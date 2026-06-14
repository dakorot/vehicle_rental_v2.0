package service;

import entities.Rental;
import entities.Vehicle;
import repository.IVehicleRepository;
import repository.impl.VehicleRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class VehicleService {
    private final IVehicleRepository vehicleRepo;
    private final RentalService rentalService;
    private final VehicleValidator vehicleValidator;

    public VehicleService(IVehicleRepository vehicleRepo, RentalService rentalService, VehicleValidator vehicleValidator) {
        this.vehicleRepo = vehicleRepo;
        this.rentalService = rentalService;
        this.vehicleValidator = vehicleValidator;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepo.getVehicles();
    }

    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> allVehicles = vehicleRepo.getVehicles();
        List<Rental> allRentals = rentalService.getAllRentals();
        List<Vehicle> availableVehicles = new ArrayList<>();

        for (Vehicle vehicle : allVehicles) {
            boolean isRented = false;
            for (Rental rental : allRentals) {
                if (rental.vehicleId.equals(vehicle.getId())) {
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
        for (Rental rental : rentalService.getAllRentals()) {
            if (rental.vehicleId.equals(vehicleId)) {
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
