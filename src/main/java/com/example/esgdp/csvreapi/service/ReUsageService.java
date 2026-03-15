package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.model.ReUsage;
import com.example.esgdp.csvreapi.repository.ReUsageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReUsageService {

    private final ReUsageRepository repository;

    public ReUsageService(ReUsageRepository repository) {
        this.repository = repository;
    }

    public List<ReUsage> getAll() {
        return repository.findAll();
    }

    public List<ReUsage> getByFab(Long fabId) {
        return repository.findByFabId(fabId);
    }

    public List<ReUsage> getByFabAndYear(Long fabId, Integer year) {
        return repository.findByFabIdAndYear(fabId, year);
    }

    public Optional<ReUsage> getByFabYearMonth(Long fabId, Integer year, Integer month) {
        return repository.findByFabIdAndYearAndMonth(fabId, year, month);
    }

    public Optional<ReUsage> getById(Long id) {
        return repository.findById(id);
    }

    public ReUsage create(ReUsage entity) {
        return repository.save(entity);
    }

    public Optional<ReUsage> update(Long id, ReUsage updated) {
        return repository.findById(id).map(existing -> {
            existing.setFab(updated.getFab());
            existing.setYear(updated.getYear());
            existing.setMonth(updated.getMonth());
            existing.setUsage(updated.getUsage());
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
