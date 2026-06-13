package repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entities.Rental;
import entities.User;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RentalRepositoryImpl implements IRentalRepository {
    private List<Rental> rentals = new ArrayList<>();
    private String fileName;

    public RentalRepositoryImpl() {
        this.fileName = "rentals.json";
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
        Gson gson = new Gson();
        try (FileWriter writer =  new FileWriter(this.fileName)) {
            gson.toJson(rentals, writer);
        } catch (IOException e) {
            System.out.println("Error occurred during save attempt: " + e.getMessage());
        }
    }

    @Override
    public void load() {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(this.fileName)) {
            Type listType = new TypeToken<ArrayList<User>>(){}.getType();
            List<Rental> loadedRentals = gson.fromJson(reader, listType);

            if (loadedRentals != null) this.rentals = loadedRentals;
            else this.rentals = new ArrayList<>();

        } catch (FileNotFoundException e) {
            System.out.println("No such file \"rentals.json\". Starting with an empty list.");
        } catch (IOException e) {
            System.out.println("Error occurred during load attempt: " + e.getMessage());
        }
    }
}
