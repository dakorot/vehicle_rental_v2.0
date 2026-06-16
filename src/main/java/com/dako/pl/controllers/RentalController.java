package com.dako.pl.controllers;

import com.dako.pl.entities.Rental;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/all")
    public List<Rental> list() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/my")
    public Rental userRentals() {
        String currentLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        return rentalService.getUserRental(currentLogin);
    }

    @PostMapping("/rent/{vehicleId}")
    public Rental rent(@PathVariable String vehicleId) {
        String currentLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        rentalService.rentVehicle(userRepo.getUser(currentLogin), vehicleRepo.getVehicle(vehicleId));
        return rentalService.getUserRental(currentLogin);
    }

    @PostMapping("/return")
    public Rental returnVehicle() {
        String currentLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        Rental returning = rentalService.getUserRental(currentLogin);
        rentalService.returnVehicle(currentLogin);
        return returning;
    }
}