package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.model.Emission;
import com.example.esgdp.csvreapi.repository.EmissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmissionService {

    private final EmissionRepository repository;

    public EmissionService(EmissionRepository repository) {
        this.repository = repository;
    }

    public List<Emission> getAll() {
        return repository.findAll();
    }

    public List<Emission> getByFab(Long fabId) {
        return repository.findByFabId(fabId);
    }

    public List<Emission> getByFabAndYear(Long fabId, Integer year) {
        return repository.findByFabIdAndYear(fabId, year);
    }

    public Optional<Emission> getByFabYearMonth(Long fabId, Integer year, Integer month) {
        return repository.findByFabIdAndYearAndMonth(fabId, year, month);
    }

    public Optional<Emission> getById(Long id) {
        return repository.findById(id);
    }

    public Emission create(Emission entity) {
        return repository.save(entity);
    }

    public Optional<Emission> update(Long id, Emission updated) {
        return repository.findById(id).map(existing -> {
            existing.setFab(updated.getFab());
            existing.setYear(updated.getYear());
            existing.setMonth(updated.getMonth());
            existing.setS1(updated.getS1());
            existing.setS2L(updated.getS2L());
            existing.setS2M(updated.getS2M());
            existing.setS3(updated.getS3());
            return repository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
