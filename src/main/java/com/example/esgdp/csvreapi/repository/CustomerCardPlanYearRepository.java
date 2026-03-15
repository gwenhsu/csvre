package com.example.esgdp.csvreapi.repository;

import com.example.esgdp.csvreapi.model.CustomerCardPlanYear;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerCardPlanYearRepository extends JpaRepository<CustomerCardPlanYear, Long> {
    List<CustomerCardPlanYear> findByPlanId(Long planId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CustomerCardPlanYear y WHERE y.plan.id = :planId")
    void deleteByPlanId(Long planId);
}
