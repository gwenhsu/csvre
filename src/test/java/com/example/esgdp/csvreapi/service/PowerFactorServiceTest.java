package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.model.Fab;
import com.example.esgdp.csvreapi.model.PowerFactor;
import com.example.esgdp.csvreapi.repository.PowerFactorRepository;
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
class PowerFactorServiceTest {

    @Mock
    private PowerFactorRepository repository;

    @InjectMocks
    private PowerFactorService service;

    private Fab fab;
    private PowerFactor pf;

    @BeforeEach
    void setUp() {
        fab = new Fab(1L, "FAB1", "Taiwan");
        pf = new PowerFactor(1L, fab, 2024, new BigDecimal("0.5000"));
    }

    @Test
    void getAll_returnsAll() {
        when(repository.findAll()).thenReturn(List.of(pf));

        assertThat(service.getAll()).hasSize(1);
    }

    @Test
    void getById_found() {
        when(repository.findById(1L)).thenReturn(Optional.of(pf));

        assertThat(service.getById(1L)).isPresent();
    }

    @Test
    void getById_notFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThat(service.getById(99L)).isEmpty();
    }

    @Test
    void getByFab_returnsFiltered() {
        when(repository.findByFabId(1L)).thenReturn(List.of(pf));

        assertThat(service.getByFab(1L)).hasSize(1);
    }

    @Test
    void getByFabAndYear_found() {
        when(repository.findByFabIdAndYear(1L, 2024)).thenReturn(Optional.of(pf));

        assertThat(service.getByFabAndYear(1L, 2024)).isPresent();
    }

    @Test
    void getByFabAndYear_notFound() {
        when(repository.findByFabIdAndYear(1L, 9999)).thenReturn(Optional.empty());

        assertThat(service.getByFabAndYear(1L, 9999)).isEmpty();
    }

    @Test
    void create_savesAndReturns() {
        when(repository.save(any(PowerFactor.class))).thenReturn(pf);

        PowerFactor result = service.create(pf);

        assertThat(result.getFactor()).isEqualByComparingTo("0.5000");
        verify(repository).save(pf);
    }

    @Test
    void update_found_updatesFields() {
        PowerFactor updated = new PowerFactor(null, fab, 2025, new BigDecimal("0.8000"));
        when(repository.findById(1L)).thenReturn(Optional.of(pf));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<PowerFactor> result = service.update(1L, updated);

        assertThat(result).isPresent();
        assertThat(result.get().getYear()).isEqualTo(2025);
        assertThat(result.get().getFactor()).isEqualByComparingTo("0.8000");
    }

    @Test
    void update_notFound_returnsEmpty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThat(service.update(99L, pf)).isEmpty();
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
