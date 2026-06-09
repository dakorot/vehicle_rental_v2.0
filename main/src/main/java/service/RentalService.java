package service;

import entities.Rental;
import entities.Vehicle;
import repository.IRentalRepository;
import repository.IVehicleRepository;

import java.util.List;
import java.util.UUID;

public class RentalService {
    private final IRentalRepository rentalRepo;
    private final IVehicleRepository vehicleRepo;

    public RentalService(IRentalRepository rentalRepo, IVehicleRepository vehicleRepo) {
        this.rentalRepo = rentalRepo;
        this.vehicleRepo = vehicleRepo;
    }

    public boolean rentVehicle(String userLogin, String vehicleId) {
        Vehicle vehicle = vehicleRepo.getVehicle(vehicleId);
        if (vehicle == null) {
            return false;
        }

        if (getUserRental(userLogin) != null) {
            return false;
        }

        for (Rental r : rentalRepo.getRentals()) {
            if (r.vehicleId.equals(vehicleId)) {
                return false;
            }
        }

        String newRentalId = UUID.randomUUID().toString();
        Rental newRental = new Rental(newRentalId, userLogin, vehicleId);

        rentalRepo.add(newRental);
        rentalRepo.save();
        return true;
    }

    public boolean returnVehicle(String userLogin) {
        Rental rental = getUserRental(userLogin);
        if (rental != null) {
            rentalRepo.remove(rental.id);
            rentalRepo.save();
            return true;
        }
        return false;
    }

    public Rental getUserRental(String userLogin) {
        for (Rental r : rentalRepo.getRentals()) {
            if (r.userLogin.equals(userLogin)) {
                return r;
            }
        }
        return null;
    }

    public List<Rental> getAllRentals() {
        return rentalRepo.getRentals();
    }
}
