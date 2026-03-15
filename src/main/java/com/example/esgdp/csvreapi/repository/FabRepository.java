package com.example.esgdp.csvreapi.repository;

import com.example.esgdp.csvreapi.model.Fab;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FabRepository extends JpaRepository<Fab, Long> {
    Optional<Fab> findByFabCode(String fabCode);
    List<Fab> findByLocation(String location);
}
