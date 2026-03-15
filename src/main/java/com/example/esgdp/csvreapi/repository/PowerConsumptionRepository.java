package com.example.esgdp.csvreapi.repository;

import com.example.esgdp.csvreapi.model.PowerConsumption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PowerConsumptionRepository extends JpaRepository<PowerConsumption, Long> {
    List<PowerConsumption> findByFabId(Long fabId);
    List<PowerConsumption> findByFabIdAndYear(Long fabId, Integer year);
    Optional<PowerConsumption> findByFabIdAndYearAndMonth(Long fabId, Integer year, Integer month);
}
