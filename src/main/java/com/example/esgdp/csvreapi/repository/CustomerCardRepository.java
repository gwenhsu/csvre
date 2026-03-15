package com.example.esgdp.csvreapi.repository;

import com.example.esgdp.csvreapi.model.CustomerCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerCardRepository extends JpaRepository<CustomerCard, String> {
}
