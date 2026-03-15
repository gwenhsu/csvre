package com.example.esgdp.csvreapi.controller;

import com.example.esgdp.csvreapi.dto.EmissionDto;
import com.example.esgdp.csvreapi.model.Emission;
import com.example.esgdp.csvreapi.model.EmissionGoal;
import com.example.esgdp.csvreapi.service.EmissionGoalService;
import com.example.esgdp.csvreapi.service.EmissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emission")
public class EmissionController {

    private final EmissionService service;
    private final EmissionGoalService goalService;

    public EmissionController(EmissionService service, EmissionGoalService goalService) {
        this.service = service;
        this.goalService = goalService;
    }

    // ── Emission ──────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<EmissionDto>> getAll(
            @RequestParam(required = false) Long fabId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        if (fabId != null && year != null && month != null) {
            return service.getByFabYearMonth(fabId, year, month)
                    .map(r -> ResponseEntity.ok(List.of(EmissionDto.from(r))))
                    .orElse(ResponseEntity.ok(List.of()));
        }
        if (fabId != null && year != null) {
            return ResponseEntity.ok(service.getByFabAndYear(fabId, year).stream().map(EmissionDto::from).toList());
        }
        if (fabId != null) {
            return ResponseEntity.ok(service.getByFab(fabId).stream().map(EmissionDto::from).toList());
        }
        return ResponseEntity.ok(service.getAll().stream().map(EmissionDto::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmissionDto> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(e -> ResponseEntity.ok(EmissionDto.from(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmissionDto> create(@Valid @RequestBody Emission entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(EmissionDto.from(service.create(entity)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmissionDto> update(@PathVariable Long id, @Valid @RequestBody Emission entity) {
        return service.update(id, entity)
                .map(e -> ResponseEntity.ok(EmissionDto.from(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // ── Emission Goal ─────────────────────────────────────

    @GetMapping("/goal")
    public ResponseEntity<List<EmissionGoal>> getAllGoals(@RequestParam(required = false) Integer year) {
        if (year != null) {
            return goalService.getByYear(year)
                    .map(r -> ResponseEntity.ok(List.of(r)))
                    .orElse(ResponseEntity.ok(List.of()));
        }
        return ResponseEntity.ok(goalService.getAll());
    }

    @GetMapping("/goal/{id}")
    public ResponseEntity<EmissionGoal> getGoalById(@PathVariable Long id) {
        return goalService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/goal")
    public ResponseEntity<EmissionGoal> createGoal(@Valid @RequestBody EmissionGoal entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(goalService.create(entity));
    }

    @PutMapping("/goal/{id}")
    public ResponseEntity<EmissionGoal> updateGoal(@PathVariable Long id, @Valid @RequestBody EmissionGoal entity) {
        return goalService.update(id, entity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/goal/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        return goalService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
