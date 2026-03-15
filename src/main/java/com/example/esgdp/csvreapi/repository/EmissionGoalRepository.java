package com.example.esgdp.csvreapi.repository;

import com.example.esgdp.csvreapi.model.EmissionGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmissionGoalRepository extends JpaRepository<EmissionGoal, Long> {
    Optional<EmissionGoal> findByYear(Integer year);
}
