package com.dako.pl.service;

import com.dako.pl.entities.Rental;
import com.dako.pl.entities.User;
import com.dako.pl.entities.Vehicle;

import java.util.List;

public interface IRentalService {
    boolean rentVehicle(User user, Vehicle vehicle);
    boolean returnVehicle(String userLogin);
    Rental getUserRental(String userLogin);
    List<Rental> getAllRentals();
    boolean isVehicleRented(String vehicleId);
}
