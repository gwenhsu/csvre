package com.example.esgdp.csvreapi.repository;

import com.example.esgdp.csvreapi.model.ReGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReGoalRepository extends JpaRepository<ReGoal, Long> {
    Optional<ReGoal> findByYear(Integer year);
}
