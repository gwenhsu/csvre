package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.model.EmissionGoal;
import com.example.esgdp.csvreapi.repository.EmissionGoalRepository;
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
class EmissionGoalServiceTest {

    @Mock
    private EmissionGoalRepository repository;

    @InjectMocks
    private EmissionGoalService service;

    private EmissionGoal goal;

    @BeforeEach
    void setUp() {
        goal = new EmissionGoal(1L, 2024,
                new BigDecimal("100.0000"),
                new BigDecimal("200.0000"),
                new BigDecimal("50.0000"),
                new BigDecimal("30.0000"));
    }

    @Test
    void getAll_returnsAll() {
        when(repository.findAll()).thenReturn(List.of(goal));

        assertThat(service.getAll()).hasSize(1);
    }

    @Test
    void getById_found() {
        when(repository.findById(1L)).thenReturn(Optional.of(goal));

        assertThat(service.getById(1L)).isPresent();
    }

    @Test
    void getById_notFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThat(service.getById(99L)).isEmpty();
    }

    @Test
    void getByYear_found() {
        when(repository.findByYear(2024)).thenReturn(Optional.of(goal));

        Optional<EmissionGoal> result = service.getByYear(2024);

        assertThat(result).isPresent();
        assertThat(result.get().getS1()).isEqualByComparingTo("100.0000");
    }

    @Test
    void getByYear_notFound() {
        when(repository.findByYear(9999)).thenReturn(Optional.empty());

        assertThat(service.getByYear(9999)).isEmpty();
    }

    @Test
    void create_savesAndReturns() {
        when(repository.save(any(EmissionGoal.class))).thenReturn(goal);

        EmissionGoal result = service.create(goal);

        assertThat(result.getYear()).isEqualTo(2024);
        verify(repository).save(goal);
    }

    @Test
    void update_found_updatesFields() {
        EmissionGoal updated = new EmissionGoal(null, 2025,
                new BigDecimal("110.0000"), new BigDecimal("220.0000"),
                new BigDecimal("55.0000"), new BigDecimal("35.0000"));
        when(repository.findById(1L)).thenReturn(Optional.of(goal));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<EmissionGoal> result = service.update(1L, updated);

        assertThat(result).isPresent();
        assertThat(result.get().getYear()).isEqualTo(2025);
        assertThat(result.get().getS2L()).isEqualByComparingTo("220.0000");
    }

    @Test
    void update_notFound_returnsEmpty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThat(service.update(99L, goal)).isEmpty();
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
