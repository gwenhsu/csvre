package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.model.EnergyType;
import com.example.esgdp.csvreapi.model.ReProcurement;
import com.example.esgdp.csvreapi.repository.ReProcurementRepository;
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
class ReProcurementServiceTest {

    @Mock
    private ReProcurementRepository repository;

    @InjectMocks
    private ReProcurementService service;

    private ReProcurement rp;

    @BeforeEach
    void setUp() {
        rp = new ReProcurement(1L, "TW", 2024, 1, 1, EnergyType.WIND, new BigDecimal("500.0000"));
    }

    @Test
    void getAll_returnsAll() {
        when(repository.findAll()).thenReturn(List.of(rp));

        assertThat(service.getAll()).hasSize(1);
    }

    @Test
    void getById_found() {
        when(repository.findById(1L)).thenReturn(Optional.of(rp));

        assertThat(service.getById(1L)).isPresent();
    }

    @Test
    void getById_notFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThat(service.getById(99L)).isEmpty();
    }

    @Test
    void getByRegion_returnsFiltered() {
        when(repository.findByRegion("TW")).thenReturn(List.of(rp));

        List<ReProcurement> result = service.getByRegion("TW");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRegion()).isEqualTo("TW");
    }

    @Test
    void getByRegionAndYear_returnsFiltered() {
        when(repository.findByRegionAndYear("TW", 2024)).thenReturn(List.of(rp));

        assertThat(service.getByRegionAndYear("TW", 2024)).hasSize(1);
    }

    @Test
    void getByRegionYearMonth_returnsFiltered() {
        when(repository.findByRegionAndYearAndMonth("TW", 2024, 1)).thenReturn(List.of(rp));

        assertThat(service.getByRegionYearMonth("TW", 2024, 1)).hasSize(1);
    }

    @Test
    void create_savesAndReturns() {
        when(repository.save(any(ReProcurement.class))).thenReturn(rp);

        ReProcurement result = service.create(rp);

        assertThat(result.getEnergyType()).isEqualTo(EnergyType.WIND);
        verify(repository).save(rp);
    }

    @Test
    void update_found_updatesFields() {
        ReProcurement updated = new ReProcurement(null, "JP", 2025, 3, 2, EnergyType.SOLAR, new BigDecimal("999.0000"));
        when(repository.findById(1L)).thenReturn(Optional.of(rp));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<ReProcurement> result = service.update(1L, updated);

        assertThat(result).isPresent();
        assertThat(result.get().getRegion()).isEqualTo("JP");
        assertThat(result.get().getEnergyType()).isEqualTo(EnergyType.SOLAR);
        assertThat(result.get().getProcurement()).isEqualByComparingTo("999.0000");
    }

    @Test
    void update_notFound_returnsEmpty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThat(service.update(99L, rp)).isEmpty();
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
