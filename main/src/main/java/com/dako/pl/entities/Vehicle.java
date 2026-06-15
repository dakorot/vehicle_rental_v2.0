package com.dako.pl.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "vehicle")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Vehicle {

    @Id
    private String id;
    private String category;
    private String brand;
    private String model;
    private int year;
    private double price;

    @Convert(converter = AttrConverter.class)
    @Column(columnDefinition = "TEXT")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Map<String, Object> attributes = new HashMap<>();

    @Builder
    public Vehicle(String id,
                   String category,
                   String brand,
                   String model,
                   int year,
                   double price,
                   Map<String, Object> attributes) {
        this.id = id;
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.attributes = attributes == null ? new HashMap<>() : new HashMap<>(attributes);
    }

    public void addAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public Vehicle copy() {
        return Vehicle.builder()
                .id(id)
                .category(category)
                .brand(brand)
                .model(model)
                .year(year)
                .price(price)
                .attributes(new HashMap<>(attributes))
                .build();
    }
}