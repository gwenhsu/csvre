package com.example.esgdp.csvreapi.controller;

import com.example.esgdp.csvreapi.model.Fab;
import com.example.esgdp.csvreapi.service.FabService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fab")
public class FabController {

    private final FabService fabService;

    public FabController(FabService fabService) {
        this.fabService = fabService;
    }

    @GetMapping
    public ResponseEntity<List<Fab>> getAll() {
        return ResponseEntity.ok(fabService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fab> getById(@PathVariable Long id) {
        return fabService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{fabCode}")
    public ResponseEntity<Fab> getByFabCode(@PathVariable String fabCode) {
        return fabService.getByFabCode(fabCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Fab> create(@Valid @RequestBody Fab fab) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fabService.create(fab));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fab> update(@PathVariable Long id, @Valid @RequestBody Fab fab) {
        return fabService.update(id, fab)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return fabService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
