package entities;

import java.util.ArrayList;

public class Car extends Vehicle {
    private final char SEPARATOR = ';';
    private final ArrayList<Car> cars = new ArrayList<Car>();

    @Override
    public String toCSV() {
        StringBuilder carsCsv = new StringBuilder();

        for (Car car : cars) {
            carsCsv.append(car.toString());
            carsCsv.append('\n');
        }

        return carsCsv.toString();
    }

    @Override
    public String toString() {
        StringBuilder carString = new StringBuilder();

        return carString.append("CAR")
                .append(SEPARATOR)
                .append(this.brand)
                .append(SEPARATOR)
                .append(this.id)
                .append(SEPARATOR)
                .append(this.model)
                .append(SEPARATOR)
                .append(this.year)
                .append(SEPARATOR)
                .append(this.price)
                .toString();
    }

    public static class Builder {
        private final String id;
        private final String brand;
        private final String model;
        private final int year;
        private final double price;

        public Builder(String id, String brand, String model, int year, double price) {
            this.id = id;
            this.brand = brand;
            this.model = model;
            this.year = year;
            this.price = price;
        }

        public Car build() {
            return new Car(this);
        }
    }

    private Car(Builder builder) {
        id = builder.id;
        brand = builder.brand;
        model = builder.model;
        year = builder.year;
        price = builder.price;
    }

    public Car(String id, String brand, String model, int year, double price, boolean rented) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
    }

    public Car(Car other) {
        this.id = other.id;
        this.brand = other.brand;
        this.model = other.model;
        this.year = other.year;
        this.price = other.price;
    }
}
