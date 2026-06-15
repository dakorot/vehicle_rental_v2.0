package com.dako.pl.service;

import com.dako.pl.entities.Vehicle;
import com.dako.pl.entities.VehicleCategoryConfig;
import com.dako.pl.service.impl.VehicleCategoryConfigServiceImpl;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VehicleValidator {

    private final VehicleCategoryConfigServiceImpl configService;

    public VehicleValidator(VehicleCategoryConfigServiceImpl configService) {
        this.configService = configService;
    }

    public void validate(Vehicle vehicle) {
        if (vehicle == null) throw new IllegalArgumentException("Vehicle cannot be null.");

        validateBaseFields(vehicle);
        validateAttributes(vehicle.getAttributes(), configService.getByCategory(vehicle.getCategory()));
    }

    private void validateBaseFields(Vehicle vehicle) {
        requireNonBlank(vehicle.getCategory(), "Category is required.");
        requireNonBlank(vehicle.getBrand(), "Brand is required.");
        requireNonBlank(vehicle.getModel(), "Model is required.");

        if (vehicle.getYear() <= 0) throw new IllegalArgumentException("Year must be a positive.");
        if (vehicle.getPrice() < 0) throw new IllegalArgumentException("Price cannot be a negative.");
    }

    private void validateAttributes(Map<String, Object> actualAttributes, VehicleCategoryConfig config) {
        Map<String, String> expectedAttributes = config.getAttributes();
        for (String actualName : actualAttributes.keySet()) {
            if (!expectedAttributes.containsKey(actualName)) {
                throw new IllegalArgumentException("Attribute is not supported for this category."
                        + config.getCategory() + ": " + actualName);
            }
        }

        expectedAttributes.forEach((attrName, expectedType) -> {
            Object value = actualAttributes.get(attrName);
            if (value == null) {
                throw new IllegalArgumentException("Required attribute not provided: " + attrName);
            }
            if (expectedType.equalsIgnoreCase("string") && value instanceof String str) {
                requireNonBlank(str, "Attribute " + attrName + " cannot be empty.");
            }

            boolean isValidType = switch (expectedType.toLowerCase()) {
                case "string" -> value instanceof String;
                case "number" -> value instanceof Number;
                case "boolean" -> value instanceof Boolean;
                case "integer" -> value instanceof Number n && n.doubleValue() % 1 == 0;
                default -> throw new IllegalArgumentException("Unsopperted type in the config: " + expectedType);
            };
            if (!isValidType) {
                throw new IllegalArgumentException("Attribute " + attrName + " must be of type " + expectedType + ".");
            }
        });
    }

    private void requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}