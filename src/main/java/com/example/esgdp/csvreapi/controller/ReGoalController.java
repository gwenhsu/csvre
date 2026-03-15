package com.example.esgdp.csvreapi.controller;

import com.example.esgdp.csvreapi.model.ReGoal;
import com.example.esgdp.csvreapi.service.ReGoalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/re-goal")
public class ReGoalController {

    private final ReGoalService service;

    public ReGoalController(ReGoalService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReGoal>> getAll(@RequestParam(required = false) Integer year) {
        if (year != null) {
            return service.getByYear(year)
                    .map(r -> ResponseEntity.ok(List.of(r)))
                    .orElse(ResponseEntity.ok(List.of()));
        }
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReGoal> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReGoal> create(@Valid @RequestBody ReGoal entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReGoal> update(@PathVariable Long id, @Valid @RequestBody ReGoal entity) {
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
