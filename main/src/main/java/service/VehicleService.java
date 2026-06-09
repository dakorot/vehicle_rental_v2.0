package service;

import entities.Rental;
import entities.Vehicle;
import repository.IVehicleRepository;

import java.util.ArrayList;
import java.util.List;

public class VehicleService {
    private final IVehicleRepository vehicleRepo;
    private final RentalService rentalService;

    public VehicleService(IVehicleRepository vehicleRepo, RentalService rentalService) {
        this.vehicleRepo = vehicleRepo;
        this.rentalService = rentalService;
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
                if (rental.vehicleId.equals(vehicle.id)) {
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
}
