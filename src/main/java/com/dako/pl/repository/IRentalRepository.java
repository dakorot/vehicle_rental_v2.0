package com.dako.pl.repository;

import com.dako.pl.entities.Rental;
import java.util.List;

public interface IRentalRepository {
    void add(Rental rental);
    void remove(String rentalId);
    List<Rental> getRentals();
    void save();
    void load();
}