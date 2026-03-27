package entities;

import java.util.ArrayList;

public class Motorcycle extends Vehicle {
    char SEPARATOR = ';';
    String category;
    ArrayList<Motorcycle> motorcycles = new ArrayList<>();

    @Override
    public String toCSV() {
        StringBuilder motorcyclesCsv = new StringBuilder();

        for (Motorcycle motorcycle : motorcycles) {
            motorcyclesCsv.append(motorcycle.toString());
            motorcyclesCsv.append('\n');
        }

        return motorcyclesCsv.toString();
    }

    @Override
    public String toString() {
        StringBuilder motoStr = new StringBuilder();

        return motoStr.append("MOTORCYCLE")
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
                .append(SEPARATOR)
                .append(this.rented)
                .append(SEPARATOR)
                .append(findCategory(this.id))
                .toString();
    }

    private String findCategory(String id) {
        for(Motorcycle moto : motorcycles) {
            if(moto.id.equals(id))
                return moto.category;
        }
        return "";
    }

    public static class Builder {
        private final String id;
        private final String brand;
        private final String model;
        private final int year;
        private final double price;
        private final boolean rented;
        private final String category;

        public Builder(String id, String brand, String model, int year, double price, boolean rented, String category) {
            this.id = id;
            this.brand = brand;
            this.model = model;
            this.year = year;
            this.price = price;
            this.rented = rented;
            this.category = category;
        }

        public Motorcycle build() {
            return new Motorcycle(this);
        }
    }
    private Motorcycle(Builder builder) {
        id = builder.id;
        brand = builder.brand;
        model = builder.model;
        year = builder.year;
        price = builder.price;
        rented = builder.rented;
        category = builder.category;
    }

    public Motorcycle(Motorcycle other) {
        this.brand = other.brand;
        this.model = other.model;
        this.year = other.year;
        this.price = other.price;
        this.rented = other.rented;
        this.category = other.category;
    }
}
