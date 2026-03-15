package com.example.esgdp.csvreapi.repository;

import com.example.esgdp.csvreapi.model.PowerFactor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PowerFactorRepository extends JpaRepository<PowerFactor, Long> {
    List<PowerFactor> findByFabId(Long fabId);
    Optional<PowerFactor> findByFabIdAndYear(Long fabId, Integer year);
}
