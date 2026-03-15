package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.model.Fab;
import com.example.esgdp.csvreapi.repository.FabRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FabService {

    private final FabRepository fabRepository;

    public FabService(FabRepository fabRepository) {
        this.fabRepository = fabRepository;
    }

    public List<Fab> getAll() {
        return fabRepository.findAll();
    }

    public Optional<Fab> getById(Long id) {
        return fabRepository.findById(id);
    }

    public Optional<Fab> getByFabCode(String fabCode) {
        return fabRepository.findByFabCode(fabCode);
    }

    public Fab create(Fab fab) {
        return fabRepository.save(fab);
    }

    public Optional<Fab> update(Long id, Fab updated) {
        return fabRepository.findById(id).map(existing -> {
            existing.setFabCode(updated.getFabCode());
            existing.setLocation(updated.getLocation());
            return fabRepository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (fabRepository.existsById(id)) {
            fabRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
