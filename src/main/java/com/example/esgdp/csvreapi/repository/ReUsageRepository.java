package com.example.esgdp.csvreapi.repository;

import com.example.esgdp.csvreapi.model.ReUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReUsageRepository extends JpaRepository<ReUsage, Long> {
    List<ReUsage> findByFabId(Long fabId);
    List<ReUsage> findByFabIdAndYear(Long fabId, Integer year);
    Optional<ReUsage> findByFabIdAndYearAndMonth(Long fabId, Integer year, Integer month);
}
