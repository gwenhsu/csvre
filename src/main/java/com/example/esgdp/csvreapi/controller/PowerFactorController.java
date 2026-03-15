package com.example.esgdp.csvreapi.controller;

import com.example.esgdp.csvreapi.dto.PowerFactorDto;
import com.example.esgdp.csvreapi.model.PowerFactor;
import com.example.esgdp.csvreapi.service.PowerFactorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/power-factor")
public class PowerFactorController {

    private final PowerFactorService service;

    public PowerFactorController(PowerFactorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PowerFactorDto>> getAll(
            @RequestParam(required = false) Long fabId,
            @RequestParam(required = false) Integer year) {

        if (fabId != null && year != null) {
            return service.getByFabAndYear(fabId, year)
                    .map(r -> ResponseEntity.ok(List.of(PowerFactorDto.from(r))))
                    .orElse(ResponseEntity.ok(List.of()));
        }
        if (fabId != null) {
            return ResponseEntity.ok(service.getByFab(fabId).stream().map(PowerFactorDto::from).toList());
        }
        return ResponseEntity.ok(service.getAll().stream().map(PowerFactorDto::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PowerFactorDto> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(e -> ResponseEntity.ok(PowerFactorDto.from(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PowerFactorDto> create(@Valid @RequestBody PowerFactor entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(PowerFactorDto.from(service.create(entity)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PowerFactorDto> update(@PathVariable Long id, @Valid @RequestBody PowerFactor entity) {
        return service.update(id, entity)
                .map(e -> ResponseEntity.ok(PowerFactorDto.from(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
