package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.model.ReGoal;
import com.example.esgdp.csvreapi.repository.ReGoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReGoalServiceTest {

    @Mock
    private ReGoalRepository repository;

    @InjectMocks
    private ReGoalService service;

    private ReGoal reGoal;

    @BeforeEach
    void setUp() {
        reGoal = new ReGoal(1L, 2024, new BigDecimal("60.0000"));
    }

    @Test
    void getAll_returnsAll() {
        when(repository.findAll()).thenReturn(List.of(reGoal));

        List<ReGoal> result = service.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getYear()).isEqualTo(2024);
    }

    @Test
    void getById_found() {
        when(repository.findById(1L)).thenReturn(Optional.of(reGoal));

        assertThat(service.getById(1L)).isPresent();
    }

    @Test
    void getById_notFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThat(service.getById(99L)).isEmpty();
    }

    @Test
    void getByYear_found() {
        when(repository.findByYear(2024)).thenReturn(Optional.of(reGoal));

        Optional<ReGoal> result = service.getByYear(2024);

        assertThat(result).isPresent();
        assertThat(result.get().getGoal()).isEqualByComparingTo("60.0000");
    }

    @Test
    void getByYear_notFound() {
        when(repository.findByYear(9999)).thenReturn(Optional.empty());

        assertThat(service.getByYear(9999)).isEmpty();
    }

    @Test
    void create_savesAndReturns() {
        when(repository.save(any(ReGoal.class))).thenReturn(reGoal);

        ReGoal result = service.create(reGoal);

        assertThat(result.getYear()).isEqualTo(2024);
        verify(repository).save(reGoal);
    }

    @Test
    void update_found_updatesFields() {
        ReGoal updated = new ReGoal(null, 2025, new BigDecimal("80.0000"));
        when(repository.findById(1L)).thenReturn(Optional.of(reGoal));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<ReGoal> result = service.update(1L, updated);

        assertThat(result).isPresent();
        assertThat(result.get().getYear()).isEqualTo(2025);
        assertThat(result.get().getGoal()).isEqualByComparingTo("80.0000");
    }

    @Test
    void update_notFound_returnsEmpty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThat(service.update(99L, reGoal)).isEmpty();
    }

    @Test
    void delete_found_returnsTrue() {
        when(repository.existsById(1L)).thenReturn(true);

        assertThat(service.delete(1L)).isTrue();
        verify(repository).deleteById(1L);
    }

    @Test
    void delete_notFound_returnsFalse() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThat(service.delete(99L)).isFalse();
    }
}
