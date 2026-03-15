package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.model.ReGoal;
import com.example.esgdp.csvreapi.repository.ReGoalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReGoalService {

    private final ReGoalRepository repository;

    public ReGoalService(ReGoalRepository repository) {
        this.repository = repository;
    }

    public List<ReGoal> getAll() {
        return repository.findAll();
    }

    public Optional<ReGoal> getByYear(Integer year) {
        return repository.findByYear(year);
    }

    public Optional<ReGoal> getById(Long id) {
        return repository.findById(id);
    }

    public ReGoal create(ReGoal entity) {
        return repository.save(entity);
    }

    public Optional<ReGoal> update(Long id, ReGoal updated) {
        return repository.findById(id).map(existing -> {
            existing.setYear(updated.getYear());
            existing.setGoal(updated.getGoal());
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
