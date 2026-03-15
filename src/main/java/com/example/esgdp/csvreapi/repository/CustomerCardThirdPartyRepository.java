package com.example.esgdp.csvreapi.repository;

import com.example.esgdp.csvreapi.model.CustomerCardThirdParty;
import com.example.esgdp.csvreapi.model.DirectionType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerCardThirdPartyRepository extends JpaRepository<CustomerCardThirdParty, Long> {
    List<CustomerCardThirdParty> findByCustomerCardCustCodeAndDirection(String custCode, DirectionType direction);

    @Modifying
    @Transactional
    @Query("DELETE FROM CustomerCardThirdParty t WHERE t.customerCard.custCode = :custCode")
    void deleteByCustomerCardCustCode(String custCode);
}
