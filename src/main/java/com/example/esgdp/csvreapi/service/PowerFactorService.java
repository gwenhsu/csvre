package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.model.PowerFactor;
import com.example.esgdp.csvreapi.repository.PowerFactorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PowerFactorService {

    private final PowerFactorRepository repository;

    public PowerFactorService(PowerFactorRepository repository) {
        this.repository = repository;
    }

    public List<PowerFactor> getAll() {
        return repository.findAll();
    }

    public List<PowerFactor> getByFab(Long fabId) {
        return repository.findByFabId(fabId);
    }

    public Optional<PowerFactor> getByFabAndYear(Long fabId, Integer year) {
        return repository.findByFabIdAndYear(fabId, year);
    }

    public Optional<PowerFactor> getById(Long id) {
        return repository.findById(id);
    }

    public PowerFactor create(PowerFactor entity) {
        return repository.save(entity);
    }

    public Optional<PowerFactor> update(Long id, PowerFactor updated) {
        return repository.findById(id).map(existing -> {
            existing.setFab(updated.getFab());
            existing.setYear(updated.getYear());
            existing.setFactor(updated.getFactor());
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
