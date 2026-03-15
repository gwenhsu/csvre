package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.model.ReProcurement;
import com.example.esgdp.csvreapi.repository.ReProcurementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReProcurementService {

    private final ReProcurementRepository repository;

    public ReProcurementService(ReProcurementRepository repository) {
        this.repository = repository;
    }

    public List<ReProcurement> getAll() {
        return repository.findAll();
    }

    public List<ReProcurement> getByRegion(String region) {
        return repository.findByRegion(region);
    }

    public List<ReProcurement> getByRegionAndYear(String region, Integer year) {
        return repository.findByRegionAndYear(region, year);
    }

    public List<ReProcurement> getByRegionYearMonth(String region, Integer year, Integer month) {
        return repository.findByRegionAndYearAndMonth(region, year, month);
    }

    public Optional<ReProcurement> getById(Long id) {
        return repository.findById(id);
    }

    public ReProcurement create(ReProcurement entity) {
        return repository.save(entity);
    }

    public Optional<ReProcurement> update(Long id, ReProcurement updated) {
        return repository.findById(id).map(existing -> {
            existing.setRegion(updated.getRegion());
            existing.setYear(updated.getYear());
            existing.setMonth(updated.getMonth());
            existing.setBundleType(updated.getBundleType());
            existing.setEnergyType(updated.getEnergyType());
            existing.setProcurement(updated.getProcurement());
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
