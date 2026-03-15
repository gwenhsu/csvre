package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.model.Emission;
import com.example.esgdp.csvreapi.model.Fab;
import com.example.esgdp.csvreapi.repository.EmissionRepository;
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
class EmissionServiceTest {

    @Mock
    private EmissionRepository repository;

    @InjectMocks
    private EmissionService service;

    private Fab fab;
    private Emission emission;

    @BeforeEach
    void setUp() {
        fab = new Fab(1L, "FAB1", "Taiwan");
        emission = new Emission(1L, fab, 2024, 1,
                new BigDecimal("100.0000"),
                new BigDecimal("200.0000"),
                new BigDecimal("50.0000"),
                new BigDecimal("30.0000"));
    }

    @Test
    void getAll_returnsAllEmissions() {
        when(repository.findAll()).thenReturn(List.of(emission));

        List<Emission> result = service.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getYear()).isEqualTo(2024);
    }

    @Test
    void getById_found() {
        when(repository.findById(1L)).thenReturn(Optional.of(emission));

        Optional<Emission> result = service.getById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    void getById_notFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<Emission> result = service.getById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void create_savesAndReturns() {
        when(repository.save(any(Emission.class))).thenReturn(emission);

        Emission result = service.create(emission);

        assertThat(result.getId()).isEqualTo(1L);
        verify(repository, times(1)).save(emission);
    }

    @Test
    void update_found_updatesFields() {
        Emission updated = new Emission(null, fab, 2025, 6,
                new BigDecimal("999.0000"), null, null, null);

        when(repository.findById(1L)).thenReturn(Optional.of(emission));
        when(repository.save(any(Emission.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<Emission> result = service.update(1L, updated);

        assertThat(result).isPresent();
        assertThat(result.get().getYear()).isEqualTo(2025);
        assertThat(result.get().getMonth()).isEqualTo(6);
        assertThat(result.get().getS1()).isEqualByComparingTo("999.0000");
    }

    @Test
    void update_notFound_returnsEmpty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<Emission> result = service.update(99L, emission);

        assertThat(result).isEmpty();
        verify(repository, never()).save(any());
    }

    @Test
    void delete_found_returnsTrue() {
        when(repository.existsById(1L)).thenReturn(true);

        boolean result = service.delete(1L);

        assertThat(result).isTrue();
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void delete_notFound_returnsFalse() {
        when(repository.existsById(99L)).thenReturn(false);

        boolean result = service.delete(99L);

        assertThat(result).isFalse();
        verify(repository, never()).deleteById(any());
    }

    @Test
    void getByFab_returnsFilteredList() {
        when(repository.findByFabId(1L)).thenReturn(List.of(emission));

        List<Emission> result = service.getByFab(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFab().getFabCode()).isEqualTo("FAB1");
    }

    @Test
    void getByFabAndYear_returnsFilteredList() {
        when(repository.findByFabIdAndYear(1L, 2024)).thenReturn(List.of(emission));

        List<Emission> result = service.getByFabAndYear(1L, 2024);

        assertThat(result).hasSize(1);
    }

    @Test
    void getByFabYearMonth_found() {
        when(repository.findByFabIdAndYearAndMonth(1L, 2024, 1)).thenReturn(Optional.of(emission));

        Optional<Emission> result = service.getByFabYearMonth(1L, 2024, 1);

        assertThat(result).isPresent();
        assertThat(result.get().getMonth()).isEqualTo(1);
    }
}
