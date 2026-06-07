package repository;

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
        this.fileName = "vehicles.txt";
        load();
    }

    public VehicleRepositoryImpl(String fileName) {
        this.fileName = fileName;
        load();
    }

    @Override
    public void rentVehicle(Vehicle vehicle) {
        vehicle.rented = true;
    }

    @Override
    public Vehicle returnVehicle(String id) {
        for(int i=0; i<cars.size(); ++i) {
            if(cars.get(i).id.equals(id)) {
                return cars.get(i);
            }
        }

        for(int i=0; i<motorcycles.size(); ++i) {
            if(motorcycles.get(i).id.equals(id)) {
                return motorcycles.get(i);
            }
        }

        return null;
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

    @Override
    public void save() {
        List<Vehicle> allVehicles = getVehicles();
        StringBuilder csv = new StringBuilder();

        for(Vehicle vehicle : allVehicles) {
            csv.append(vehicle.toCSV());
            csv.append('\n');
        }

        try {
            File file = new File(this.fileName);
            FileWriter fr = new FileWriter(file, true);
            PrintWriter printWriter = new PrintWriter(fr);
            printWriter.write(csv.toString());
            printWriter.flush();
            printWriter.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        } {}
    }

    @Override
    public void load() {
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        try(BufferedReader br = new BufferedReader(new FileReader(this.fileName))) {
            String line = "";
            while((line = br.readLine()) != null) {
                String[] values = line.split(SEPARATOR);
                    if(values[0].charAt(0) == 'C') {
                        Car car = new Car.Builder(values[1], values[2], values[3], Integer.parseInt(values[4]), Double.parseDouble(values[5]), Boolean.parseBoolean(values[6])).build();
                        cars.add(car);
                    }
                    else {
                        Motorcycle motorcycle = new Motorcycle.Builder(values[1], values[2], values[3], Integer.parseInt(values[4]), Double.parseDouble(values[5]), Boolean.parseBoolean(values[6]), values[7]).build();
                        motorcycles.add(motorcycle);
                    }
                }
            } catch (IOException ex) {
            System.out.println("File does not exist or is empty: " + ex.getMessage());;
        }

        // for unit tests
        if (cars.isEmpty() && motorcycles.isEmpty()) {
            Car dummyCar = new Car.Builder("1", "Test", "Vehicle", 2020, 100.0, false).build();
            this.add(dummyCar);
        }
    }

    @Override
    public void add(Vehicle vehicle) {
        if (vehicle instanceof Car) {
            vehicle.id = Integer.toString((cars.size() + 1));
            cars.add((Car) vehicle);
        } else if (vehicle instanceof Motorcycle) {
            vehicle.id = Integer.toString((motorcycles.size() + 1));
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
}
