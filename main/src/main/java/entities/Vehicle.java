package entities;

public abstract class Vehicle {
    String id;
    String brand;
    String model;
    int year;
    double price;
    boolean rented;

    public abstract String toCSV();
    public abstract String toString();

    public boolean isRented() {
        return this.rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }
}
