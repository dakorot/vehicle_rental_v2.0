package service;

import entities.Rental;
import entities.User;
import entities.Vehicle;

import java.util.List;

public interface IRentalService {
    boolean rentVehicle(User user, Vehicle vehicle);
    boolean returnVehicle(String userLogin);
    Rental getUserRental(String userLogin);
    List<Rental> getAllRentals();
    boolean isVehicleRented(String vehicleId);
}
