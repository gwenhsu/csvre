package com.example.esgdp.csvreapi.repository;

import com.example.esgdp.csvreapi.model.CustomerCardParticipateOption;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerCardParticipateOptionRepository extends JpaRepository<CustomerCardParticipateOption, Long> {
    List<CustomerCardParticipateOption> findByCustomerCardCustCode(String custCode);

    @Modifying
    @Transactional
    @Query("DELETE FROM CustomerCardParticipateOption o WHERE o.customerCard.custCode = :custCode")
    void deleteByCustomerCardCustCode(String custCode);
}
