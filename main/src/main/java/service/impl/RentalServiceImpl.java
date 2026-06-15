package service.impl;

import entities.Rental;
import entities.User;
import entities.Vehicle;
import repository.IRentalRepository;
import repository.IVehicleRepository;
import service.IRentalService;

import java.util.List;
import java.util.UUID;

public class RentalServiceImpl implements IRentalService {
    private final IRentalRepository rentalRepo;
    private final IVehicleRepository vehicleRepo;

    public RentalServiceImpl(IRentalRepository rentalRepo, IVehicleRepository vehicleRepo) {
        this.rentalRepo = rentalRepo;
        this.vehicleRepo = vehicleRepo;
    }

    public boolean rentVehicle(User user, Vehicle vehicle) {
        Vehicle rentedVehicle = vehicleRepo.getVehicle(vehicle.getId());
        if (rentedVehicle == null) {
            return false;
        }

        if (getUserRental(user.getLogin()) != null) {
            return false;
        }

        for (Rental r : rentalRepo.getRentals()) {
            if (r.vehicle.getId().equals(vehicle.getId())) {
                return false;
            }
        }

        String newRentalId = UUID.randomUUID().toString();
        Rental newRental = new Rental(newRentalId, user, rentedVehicle);

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
            if (r.user.getLogin().equals(userLogin)) {
                return r;
            }
        }
        return null;
    }

    public List<Rental> getAllRentals() {
        return rentalRepo.getRentals();
    }

    public boolean isVehicleRented(String vehicleId) {
        for(Rental rental : rentalRepo.getRentals()) {
            if(vehicleId.equals(rental.id)) return true;
        }
        return false;
    }
}
