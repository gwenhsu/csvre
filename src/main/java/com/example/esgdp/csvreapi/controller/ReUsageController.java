package com.example.esgdp.csvreapi.controller;

import com.example.esgdp.csvreapi.dto.ReUsageDto;
import com.example.esgdp.csvreapi.model.ReUsage;
import com.example.esgdp.csvreapi.service.ReUsageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/re-usage")
public class ReUsageController {

    private final ReUsageService service;

    public ReUsageController(ReUsageService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReUsageDto>> getAll(
            @RequestParam(required = false) Long fabId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        if (fabId != null && year != null && month != null) {
            return service.getByFabYearMonth(fabId, year, month)
                    .map(r -> ResponseEntity.ok(List.of(ReUsageDto.from(r))))
                    .orElse(ResponseEntity.ok(List.of()));
        }
        if (fabId != null && year != null) {
            return ResponseEntity.ok(service.getByFabAndYear(fabId, year).stream().map(ReUsageDto::from).toList());
        }
        if (fabId != null) {
            return ResponseEntity.ok(service.getByFab(fabId).stream().map(ReUsageDto::from).toList());
        }
        return ResponseEntity.ok(service.getAll().stream().map(ReUsageDto::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReUsageDto> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(e -> ResponseEntity.ok(ReUsageDto.from(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReUsageDto> create(@Valid @RequestBody ReUsage entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ReUsageDto.from(service.create(entity)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReUsageDto> update(@PathVariable Long id, @Valid @RequestBody ReUsage entity) {
        return service.update(id, entity)
                .map(e -> ResponseEntity.ok(ReUsageDto.from(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
