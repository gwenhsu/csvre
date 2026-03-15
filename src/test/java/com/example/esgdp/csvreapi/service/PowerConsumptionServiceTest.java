package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.model.Fab;
import com.example.esgdp.csvreapi.model.PowerConsumption;
import com.example.esgdp.csvreapi.repository.PowerConsumptionRepository;
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
class PowerConsumptionServiceTest {

    @Mock
    private PowerConsumptionRepository repository;

    @InjectMocks
    private PowerConsumptionService service;

    private Fab fab;
    private PowerConsumption pc;

    @BeforeEach
    void setUp() {
        fab = new Fab(1L, "FAB1", "Taiwan");
        pc = new PowerConsumption(1L, fab, 2024, 1, new BigDecimal("1500.0000"));
    }

    @Test
    void getAll_returnsAll() {
        when(repository.findAll()).thenReturn(List.of(pc));

        assertThat(service.getAll()).hasSize(1);
    }

    @Test
    void getById_found() {
        when(repository.findById(1L)).thenReturn(Optional.of(pc));

        assertThat(service.getById(1L)).isPresent();
    }

    @Test
    void getById_notFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThat(service.getById(99L)).isEmpty();
    }

    @Test
    void getByFab_returnsFiltered() {
        when(repository.findByFabId(1L)).thenReturn(List.of(pc));

        assertThat(service.getByFab(1L)).hasSize(1);
    }

    @Test
    void getByFabAndYear_returnsFiltered() {
        when(repository.findByFabIdAndYear(1L, 2024)).thenReturn(List.of(pc));

        assertThat(service.getByFabAndYear(1L, 2024)).hasSize(1);
    }

    @Test
    void getByFabYearMonth_found() {
        when(repository.findByFabIdAndYearAndMonth(1L, 2024, 1)).thenReturn(Optional.of(pc));

        assertThat(service.getByFabYearMonth(1L, 2024, 1)).isPresent();
    }

    @Test
    void create_savesAndReturns() {
        when(repository.save(any(PowerConsumption.class))).thenReturn(pc);

        PowerConsumption result = service.create(pc);

        assertThat(result.getPower()).isEqualByComparingTo("1500.0000");
        verify(repository).save(pc);
    }

    @Test
    void update_found_updatesFields() {
        PowerConsumption updated = new PowerConsumption(null, fab, 2025, 6, new BigDecimal("2000.0000"));
        when(repository.findById(1L)).thenReturn(Optional.of(pc));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<PowerConsumption> result = service.update(1L, updated);

        assertThat(result).isPresent();
        assertThat(result.get().getYear()).isEqualTo(2025);
        assertThat(result.get().getPower()).isEqualByComparingTo("2000.0000");
    }

    @Test
    void update_notFound_returnsEmpty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThat(service.update(99L, pc)).isEmpty();
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
        verify(repository, never()).deleteById(any());
    }
}
