package com.example.esgdp.csvreapi.repository;

import com.example.esgdp.csvreapi.model.EnergyType;
import com.example.esgdp.csvreapi.model.ReProcurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReProcurementRepository extends JpaRepository<ReProcurement, Long> {
    List<ReProcurement> findByRegion(String region);
    List<ReProcurement> findByRegionAndYear(String region, Integer year);
    List<ReProcurement> findByRegionAndYearAndMonth(String region, Integer year, Integer month);
    Optional<ReProcurement> findByRegionAndYearAndMonthAndEnergyType(String region, Integer year, Integer month, EnergyType energyType);
}
