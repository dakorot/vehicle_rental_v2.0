package entities;

import lombok.*;
import repository.IVehicleRepository;
import service.RentalService;
import service.VehicleValidator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
public class Vehicle {
    private String id;
    private String category;
    private String brand;
    private String model;
    private int year;
    private double price;

    private final IVehicleRepository vehicleRepo;
    private final RentalService rentalService;
    private final VehicleValidator validator;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Map<String, Object> attributes = new HashMap<>();

    @Builder
    public Vehicle(String id,
                   String category,
                   String brand,
                   String model,
                   int year,
                   String plate,
                   double price,
                   Map<String, Object> attributes,
                   IVehicleRepository vehicleRepo,
                   RentalService rentalService,
                   VehicleValidator validator) {
        this.id = id;
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.attributes = attributes == null ? new HashMap<>() : new HashMap<>(attributes);

        this.vehicleRepo = vehicleRepo;
        this.rentalService = rentalService;
        this.validator = validator;
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
                .vehicleRepo(vehicleRepo)
                .rentalService(rentalService)
                .validator(validator)
                .build();
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        validator.validate(vehicle);
        vehicleRepo.add(vehicle);
        vehicleRepo.save();
        return vehicle;
    }

    public void removeVehicle(String vehicleId) {
        if (rentalService.isVehicleRented(vehicleId)) {
            throw new IllegalStateException("You cannot rent this vehicle because it has been already rented!");
        }
        vehicleRepo.remove(vehicleId);
        vehicleRepo.save();
    }
}
