package com.example.esgdp.csvreapi.controller;

import com.example.esgdp.csvreapi.model.ReProcurement;
import com.example.esgdp.csvreapi.service.ReProcurementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/re-procurement")
public class ReProcurementController {

    private final ReProcurementService service;

    public ReProcurementController(ReProcurementService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReProcurement>> getAll(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        if (region != null && year != null && month != null) {
            return ResponseEntity.ok(service.getByRegionYearMonth(region, year, month));
        }
        if (region != null && year != null) {
            return ResponseEntity.ok(service.getByRegionAndYear(region, year));
        }
        if (region != null) {
            return ResponseEntity.ok(service.getByRegion(region));
        }
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReProcurement> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReProcurement> create(@Valid @RequestBody ReProcurement entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReProcurement> update(@PathVariable Long id, @Valid @RequestBody ReProcurement entity) {
        return service.update(id, entity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
