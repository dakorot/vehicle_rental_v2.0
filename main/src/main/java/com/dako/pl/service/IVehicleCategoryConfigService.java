package com.dako.pl.service;

import com.dako.pl.entities.VehicleCategoryConfig;

import java.util.List;

public interface IVehicleCategoryConfigService {
    List<VehicleCategoryConfig> findAllCategories();
    VehicleCategoryConfig getByCategory(String category);
    boolean categoryExists(String category);
}
