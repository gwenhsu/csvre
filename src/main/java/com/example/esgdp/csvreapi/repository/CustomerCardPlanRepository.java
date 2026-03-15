package com.example.esgdp.csvreapi.repository;

import com.example.esgdp.csvreapi.model.CustomerCardPlan;
import com.example.esgdp.csvreapi.model.PlanType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerCardPlanRepository extends JpaRepository<CustomerCardPlan, Long> {
    List<CustomerCardPlan> findByCustomerCardCustCode(String custCode);
    Optional<CustomerCardPlan> findByCustomerCardCustCodeAndPlanType(String custCode, PlanType planType);

    @Modifying
    @Transactional
    @Query("DELETE FROM CustomerCardPlan p WHERE p.customerCard.custCode = :custCode")
    void deleteByCustomerCardCustCode(String custCode);
}
