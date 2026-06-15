package com.dako.pl.repository;

import com.dako.pl.entities.VehicleCategoryConfig;

import java.util.List;
import java.util.Optional;

public interface VehicleCategoryConfigRepository {
    List<VehicleCategoryConfig> findAll();
    Optional<VehicleCategoryConfig> findByCategory(String category);
}