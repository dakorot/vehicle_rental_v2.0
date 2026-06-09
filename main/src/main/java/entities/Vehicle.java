package entities;

public abstract class Vehicle {
    public String id;
    String brand;
    String model;
    int year;
    double price;

    public abstract String toCSV();
    public abstract String toString();
}
