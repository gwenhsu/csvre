package com.example.esgdp.csvreapi.repository;

import com.example.esgdp.csvreapi.model.Emission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmissionRepository extends JpaRepository<Emission, Long> {
    List<Emission> findByFabId(Long fabId);
    List<Emission> findByFabIdAndYear(Long fabId, Integer year);
    Optional<Emission> findByFabIdAndYearAndMonth(Long fabId, Integer year, Integer month);
}
