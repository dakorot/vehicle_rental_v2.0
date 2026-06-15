package com.dako.pl.controllers;

import com.dako.pl.entities.Rental;
import org.springframework.web.bind.annotation.*;
import com.dako.pl.repository.IUserRepository;
import com.dako.pl.repository.IVehicleRepository;
import com.dako.pl.service.IRentalService;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final IRentalService rentalService;
    private final IUserRepository userRepo;
    private final IVehicleRepository vehicleRepo;

    public RentalController(IRentalService rentalService, IUserRepository userRepo, IVehicleRepository vehicleRepo) {
        this.rentalService = rentalService;
        this.userRepo = userRepo;
        this.vehicleRepo = vehicleRepo;
    }

    @GetMapping
    public List<Rental> list() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/users/{userId}")
    public List<Rental> userRentals(@PathVariable String userId) {
        return List.of(rentalService.getUserRental(userId));
    }

    @PostMapping("/users/{userId}/rent/{vehicleId}")
    public Rental rent(@PathVariable String userId, @PathVariable String vehicleId) {
        rentalService.rentVehicle(userRepo.getUser(userId), vehicleRepo.getVehicle(vehicleId));
        return rentalService.getUserRental(userId);
    }

    @PostMapping("/users/{userId}/return")
    public Rental returnVehicle(@PathVariable String userId) {
        Rental returning = rentalService.getUserRental(userId);
        rentalService.returnVehicle(userId);
        return returning;
    }
}