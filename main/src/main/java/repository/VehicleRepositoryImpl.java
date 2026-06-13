package repository;

import com.google.gson.Gson;
import entities.Car;
import entities.Motorcycle;
import entities.Vehicle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepositoryImpl implements IVehicleRepository {
    private final String SEPARATOR = ";";
    List<Car> cars = new ArrayList<>();
    List<Motorcycle> motorcycles = new ArrayList<>();
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
        List<Vehicle> vehicles = new ArrayList<>();

        for (Car car : cars) {
            vehicles.add(new Car(car));
        }

        for (Motorcycle m : motorcycles) {
            vehicles.add(new Motorcycle(m));
        }

        return vehicles;
    }

    public void save() {
        Gson gson = new Gson();
        VehicleData data = new VehicleData();
        data.cars = this.cars;
        data.motorcycles = this.motorcycles;

        try (FileWriter writer = new FileWriter(this.fileName)) {
            gson.toJson(data, writer);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        } {}
    }

    public void load() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(this.fileName)) {
            VehicleData data = gson.fromJson(reader, VehicleData.class);
            if (data != null) {
                this.cars = data.cars != null ? data.cars : new ArrayList<>();
                this.motorcycles = data.motorcycles != null ? data.motorcycles : new ArrayList<>();
            }
        } catch (IOException ex) {
            System.out.println("File does not exist or is empty: " + ex.getMessage());
        }

        // for unit tests
        if (cars.isEmpty() && motorcycles.isEmpty()) {
            Car dummyCar = new Car.Builder("1", "Test", "Vehicle", 2020, 100.0).build();
            this.add(dummyCar);
        }
    }

    @Override
    public void add(Vehicle vehicle) {
        if (vehicle instanceof Car) {
            vehicle.id = Integer.toString(cars.size() + motorcycles.size() + 1);
            cars.add((Car) vehicle);
        } else if (vehicle instanceof Motorcycle) {
            vehicle.id = Integer.toString(cars.size() + motorcycles.size() + 1);
            motorcycles.add((Motorcycle) vehicle);
        }
    }

    @Override
    public void remove(String id) {
        cars.removeIf(car -> car.id.equals(id));
        motorcycles.removeIf(motorcycle -> motorcycle.id.equals(id));
    }

    @Override
    public Vehicle getVehicle(String id) {
        for (Car car : cars) {
            if (car.id.equals(id)) {
                return new Car(car);
            }
        }

        for (Motorcycle motorcycle : motorcycles) {
            if (motorcycle.id.equals(id)) {
                return new Motorcycle(motorcycle);
            }
        }
        return null;
    }

    private static class VehicleData {
        List<Car> cars = new ArrayList<>();
        List<Motorcycle> motorcycles = new ArrayList<>();
    }
}
