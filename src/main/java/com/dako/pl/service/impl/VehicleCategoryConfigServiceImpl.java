package com.dako.pl.service.impl;


import com.dako.pl.entities.VehicleCategoryConfig;
import com.dako.pl.repository.impl.VehicleCategoryConfigRepositoryImpl;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.dako.pl.repository.VehicleCategoryConfigRepository;
import com.dako.pl.service.IVehicleCategoryConfigService;

import java.util.List;

@Service
@Transactional
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
                .orElseThrow(() -> new IllegalArgumentException("Unknown vehicle category: " + category));
    }

    public boolean categoryExists(String category) {
        return configRepository.findByCategory(category).isPresent();
    }
}