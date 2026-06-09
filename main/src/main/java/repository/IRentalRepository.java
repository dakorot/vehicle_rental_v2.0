package repository;

import entities.Rental;
import java.util.List;

public interface IRentalRepository {
    void add(Rental rental);
    void remove(String rentalId);
    List<Rental> getRentals();
    void save();
    void load();
}