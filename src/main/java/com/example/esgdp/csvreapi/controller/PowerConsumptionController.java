package com.example.esgdp.csvreapi.controller;

import com.example.esgdp.csvreapi.dto.PowerConsumptionDto;
import com.example.esgdp.csvreapi.model.PowerConsumption;
import com.example.esgdp.csvreapi.service.PowerConsumptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/power-consumption")
public class PowerConsumptionController {

    private final PowerConsumptionService service;

    public PowerConsumptionController(PowerConsumptionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PowerConsumptionDto>> getAll(
            @RequestParam(required = false) Long fabId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        if (fabId != null && year != null && month != null) {
            return service.getByFabYearMonth(fabId, year, month)
                    .map(r -> ResponseEntity.ok(List.of(PowerConsumptionDto.from(r))))
                    .orElse(ResponseEntity.ok(List.of()));
        }
        if (fabId != null && year != null) {
            return ResponseEntity.ok(service.getByFabAndYear(fabId, year).stream().map(PowerConsumptionDto::from).toList());
        }
        if (fabId != null) {
            return ResponseEntity.ok(service.getByFab(fabId).stream().map(PowerConsumptionDto::from).toList());
        }
        return ResponseEntity.ok(service.getAll().stream().map(PowerConsumptionDto::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PowerConsumptionDto> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(e -> ResponseEntity.ok(PowerConsumptionDto.from(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PowerConsumptionDto> create(@Valid @RequestBody PowerConsumption entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(PowerConsumptionDto.from(service.create(entity)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PowerConsumptionDto> update(@PathVariable Long id, @Valid @RequestBody PowerConsumption entity) {
        return service.update(id, entity)
                .map(e -> ResponseEntity.ok(PowerConsumptionDto.from(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
