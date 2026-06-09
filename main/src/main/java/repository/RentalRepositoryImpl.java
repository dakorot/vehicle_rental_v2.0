package repository;

import entities.Rental;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RentalRepositoryImpl implements IRentalRepository {
    private List<Rental> rentals = new ArrayList<>();
    private String fileName;

    public RentalRepositoryImpl() {
        this.fileName = "vehicles.txt";
        load();
    }

    @Override
    public void add(Rental rental) {
        rentals.add(rental);
    }

    @Override
    public void remove(String rentalId) {
        rentals.removeIf(r -> r.id.equals(rentalId));
    }

    @Override
    public List<Rental> getRentals() {
        return new ArrayList<>(rentals);
    }

    @Override
    public void save() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(this.fileName, false))) {
            for (Rental r : rentals) {
                writer.println(r.id + ";" + r.userLogin + ";" + r.vehicleId);
            }
        } catch (IOException e) {
            System.out.println("Error occurred during save attempt: " + e.getMessage());
        }
    }

    @Override
    public void load() {
        rentals.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(this.fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length == 3) {
                    rentals.add(new Rental(data[0], data[1], data[2]));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No such file \"rentals.txt\". Starting with an empty list.");
        } catch (IOException e) {
            System.out.println("Error occurred during load attempt: " + e.getMessage());
        }
    }
}
