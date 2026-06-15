package service.impl;


import entities.VehicleCategoryConfig;
import repository.VehicleCategoryConfigRepository;
import service.IVehicleCategoryConfigService;

import java.util.List;

public class VehicleCategoryConfigServiceImpl implements IVehicleCategoryConfigService {

    private final VehicleCategoryConfigRepository configRepository;

    public VehicleCategoryConfigServiceImpl(VehicleCategoryConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public List<VehicleCategoryConfig> findAllCategories() {
        return configRepository.findAll();
    }

    public VehicleCategoryConfig getByCategory(String category) {
        return configRepository.findByCategory(category)
                .orElseThrow(() -> new IllegalArgumentException("Nieznana kategoria pojazdu: " + category));
    }

    public boolean categoryExists(String category) {
        return configRepository.findByCategory(category).isPresent();
    }
}