package com.dako.pl.controllers;

import com.dako.pl.entities.Vehicle;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dako.pl.service.IVehicleService;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final IVehicleService vehicleService;

    public VehicleController(IVehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<Vehicle> list(@RequestParam(name = "available", required = false, defaultValue = "false") boolean available) {
        return available ? vehicleService.getAvailableVehicles() : vehicleService.getAllVehicles();
    }

    @GetMapping("/{id}")
    public Vehicle get(@PathVariable String id) {
        return vehicleService.getVehicle(id);
    }

    @PostMapping
    public Vehicle create(@RequestBody Vehicle vehicle) {
        return vehicleService.addVehicle(vehicle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        vehicleService.removeVehicle(id);
        return ResponseEntity.noContent().build();
    }
}