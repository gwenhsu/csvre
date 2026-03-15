package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.model.Fab;
import com.example.esgdp.csvreapi.model.ReUsage;
import com.example.esgdp.csvreapi.repository.ReUsageRepository;
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
class ReUsageServiceTest {

    @Mock
    private ReUsageRepository repository;

    @InjectMocks
    private ReUsageService service;

    private Fab fab;
    private ReUsage reUsage;

    @BeforeEach
    void setUp() {
        fab = new Fab(1L, "FAB1", "Taiwan");
        reUsage = new ReUsage(1L, fab, 2024, 1, new BigDecimal("800.0000"));
    }

    @Test
    void getAll_returnsAll() {
        when(repository.findAll()).thenReturn(List.of(reUsage));

        assertThat(service.getAll()).hasSize(1);
    }

    @Test
    void getById_found() {
        when(repository.findById(1L)).thenReturn(Optional.of(reUsage));

        assertThat(service.getById(1L)).isPresent();
    }

    @Test
    void getById_notFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThat(service.getById(99L)).isEmpty();
    }

    @Test
    void getByFab_returnsFiltered() {
        when(repository.findByFabId(1L)).thenReturn(List.of(reUsage));

        assertThat(service.getByFab(1L)).hasSize(1);
    }

    @Test
    void getByFabAndYear_returnsFiltered() {
        when(repository.findByFabIdAndYear(1L, 2024)).thenReturn(List.of(reUsage));

        assertThat(service.getByFabAndYear(1L, 2024)).hasSize(1);
    }

    @Test
    void getByFabYearMonth_found() {
        when(repository.findByFabIdAndYearAndMonth(1L, 2024, 1)).thenReturn(Optional.of(reUsage));

        assertThat(service.getByFabYearMonth(1L, 2024, 1)).isPresent();
    }

    @Test
    void create_savesAndReturns() {
        when(repository.save(any(ReUsage.class))).thenReturn(reUsage);

        ReUsage result = service.create(reUsage);

        assertThat(result.getUsage()).isEqualByComparingTo("800.0000");
        verify(repository).save(reUsage);
    }

    @Test
    void update_found_updatesFields() {
        ReUsage updated = new ReUsage(null, fab, 2025, 3, new BigDecimal("1200.0000"));
        when(repository.findById(1L)).thenReturn(Optional.of(reUsage));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<ReUsage> result = service.update(1L, updated);

        assertThat(result).isPresent();
        assertThat(result.get().getYear()).isEqualTo(2025);
        assertThat(result.get().getUsage()).isEqualByComparingTo("1200.0000");
    }

    @Test
    void update_notFound_returnsEmpty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThat(service.update(99L, reUsage)).isEmpty();
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
