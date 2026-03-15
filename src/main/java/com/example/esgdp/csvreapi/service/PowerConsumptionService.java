package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.model.PowerConsumption;
import com.example.esgdp.csvreapi.repository.PowerConsumptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PowerConsumptionService {

    private final PowerConsumptionRepository repository;

    public PowerConsumptionService(PowerConsumptionRepository repository) {
        this.repository = repository;
    }

    public List<PowerConsumption> getAll() {
        return repository.findAll();
    }

    public List<PowerConsumption> getByFab(Long fabId) {
        return repository.findByFabId(fabId);
    }

    public List<PowerConsumption> getByFabAndYear(Long fabId, Integer year) {
        return repository.findByFabIdAndYear(fabId, year);
    }

    public Optional<PowerConsumption> getByFabYearMonth(Long fabId, Integer year, Integer month) {
        return repository.findByFabIdAndYearAndMonth(fabId, year, month);
    }

    public Optional<PowerConsumption> getById(Long id) {
        return repository.findById(id);
    }

    public PowerConsumption create(PowerConsumption entity) {
        return repository.save(entity);
    }

    public Optional<PowerConsumption> update(Long id, PowerConsumption updated) {
        return repository.findById(id).map(existing -> {
            existing.setFab(updated.getFab());
            existing.setYear(updated.getYear());
            existing.setMonth(updated.getMonth());
            existing.setPower(updated.getPower());
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
