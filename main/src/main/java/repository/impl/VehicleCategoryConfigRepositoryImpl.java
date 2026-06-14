package repository.impl;

import com.google.gson.reflect.TypeToken;
import db.JsonFileStorage;
import repository.VehicleCategoryConfigRepository;
import entities.VehicleCategoryConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehicleCategoryConfigRepositoryImpl implements VehicleCategoryConfigRepository {
    private final JsonFileStorage<VehicleCategoryConfig> storage =
            new JsonFileStorage<>("categories.json",
                    new TypeToken<List<VehicleCategoryConfig>>() {}.getType());

    private final List<VehicleCategoryConfig> configs;

    public VehicleCategoryConfigRepositoryImpl() {
        this.configs = new ArrayList<>(storage.load());
    }

    @Override
    public List<VehicleCategoryConfig> findAll() {
        List<VehicleCategoryConfig> copy = new ArrayList<>();
        for (VehicleCategoryConfig config : configs) {
            copy.add(config.copy());
        }
        return copy;
    }

    @Override
    public Optional<VehicleCategoryConfig> findByCategory(String category) {
        return configs.stream()
                .filter(c -> c.getCategory() != null)
                .filter(c -> c.getCategory().equalsIgnoreCase(category))
                .findFirst()
                .map(VehicleCategoryConfig::copy);
    }
}
