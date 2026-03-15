package com.example.esgdp.csvreapi.controller;

import com.example.esgdp.csvreapi.dto.CustomerCardDto;
import com.example.esgdp.csvreapi.service.CustomerCardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-card")
public class CustomerCardController {

    private final CustomerCardService service;

    public CustomerCardController(CustomerCardService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CustomerCardDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{custCode}")
    public ResponseEntity<CustomerCardDto> getByCustCode(@PathVariable String custCode) {
        return service.getByCustCode(custCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CustomerCardDto> create(@Valid @RequestBody CustomerCardDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{custCode}")
    public ResponseEntity<CustomerCardDto> update(@PathVariable String custCode, @Valid @RequestBody CustomerCardDto dto) {
        return service.update(custCode, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
