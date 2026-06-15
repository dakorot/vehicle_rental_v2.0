package repository.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entities.Vehicle;
import repository.IVehicleRepository;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepositoryImpl implements IVehicleRepository {
    private List<Vehicle> vehicles = new ArrayList<>();
    private String fileName;

    public VehicleRepositoryImpl() {
        this.fileName = "vehicles.json";
        load();
    }

    public VehicleRepositoryImpl(String fileName) {
        this.fileName = fileName;
        load();
    }

    @Override
    public List<Vehicle> getVehicles() {
        List<Vehicle> copyList = new ArrayList<>();
        for (Vehicle v : vehicles) {
            copyList.add(v.copy());
        }
        return copyList;
    }

    public void save() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(this.fileName)) {
            gson.toJson(vehicles, writer);
        } catch (IOException e) {
            throw new RuntimeException("Błąd zapisu do pliku: " + e.getMessage(), e);
        }
    }

    public void load() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(this.fileName)) {
            Type listType = new TypeToken<ArrayList<Vehicle>>(){}.getType();
            List<Vehicle> loadedVehicles = gson.fromJson(reader, listType);

            if (loadedVehicles != null) {
                this.vehicles = loadedVehicles;
            } else {
                this.vehicles = new ArrayList<>();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Plik " + this.fileName + " nie istnieje. Startujemy z pustą listą.");
            this.vehicles = new ArrayList<>();
        } catch (IOException ex) {
            System.out.println("Błąd podczas odczytu: " + ex.getMessage());
        }

        if (vehicles.isEmpty()) {
            Vehicle dummyVehicle = Vehicle.builder()
                    .id("1")
                    .category("Car")
                    .brand("Test")
                    .model("Vehicle")
                    .year(2020)
                    .price(100.0)
                    .build();
            this.add(dummyVehicle);
        }
    }

    private String generateNextId() {
        int maxId = 0;
        for (Vehicle v : vehicles) {
            try {
                int currentId = Integer.parseInt(v.getId());
                if (currentId > maxId) {
                    maxId = currentId;
                }
            } catch (NumberFormatException e) {
            }
        }
        return Integer.toString(maxId + 1);
    }

    @Override
    public void add(Vehicle vehicle) {
        vehicle.setId(generateNextId());
        vehicles.add(vehicle);
    }

    @Override
    public void remove(String id) {
        vehicles.removeIf(vehicle -> vehicle.getId().equals(id));
    }

    @Override
    public Vehicle getVehicle(String id) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId().equals(id)) {
                return vehicle.copy();
            }
        }
        return null;
    }
}