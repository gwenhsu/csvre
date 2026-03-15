package com.example.esgdp.csvreapi.repository;

import com.example.esgdp.csvreapi.model.CustomerCardRePreference;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerCardRePreferenceRepository extends JpaRepository<CustomerCardRePreference, Long> {
    List<CustomerCardRePreference> findByCustomerCardCustCode(String custCode);

    @Modifying
    @Transactional
    @Query("DELETE FROM CustomerCardRePreference r WHERE r.customerCard.custCode = :custCode")
    void deleteByCustomerCardCustCode(String custCode);
}
