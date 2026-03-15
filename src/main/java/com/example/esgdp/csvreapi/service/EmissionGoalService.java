package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.model.EmissionGoal;
import com.example.esgdp.csvreapi.repository.EmissionGoalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmissionGoalService {

    private final EmissionGoalRepository repository;

    public EmissionGoalService(EmissionGoalRepository repository) {
        this.repository = repository;
    }

    public List<EmissionGoal> getAll() {
        return repository.findAll();
    }

    public Optional<EmissionGoal> getByYear(Integer year) {
        return repository.findByYear(year);
    }

    public Optional<EmissionGoal> getById(Long id) {
        return repository.findById(id);
    }

    public EmissionGoal create(EmissionGoal entity) {
        return repository.save(entity);
    }

    public Optional<EmissionGoal> update(Long id, EmissionGoal updated) {
        return repository.findById(id).map(existing -> {
            existing.setYear(updated.getYear());
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
