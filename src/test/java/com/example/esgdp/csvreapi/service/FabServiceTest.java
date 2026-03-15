package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.model.Fab;
import com.example.esgdp.csvreapi.repository.FabRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FabServiceTest {

    @Mock
    private FabRepository fabRepository;

    @InjectMocks
    private FabService service;

    private Fab fab;

    @BeforeEach
    void setUp() {
        fab = new Fab(1L, "FAB1", "Taiwan");
    }

    @Test
    void getAll_returnsAll() {
        when(fabRepository.findAll()).thenReturn(List.of(fab));

        List<Fab> result = service.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFabCode()).isEqualTo("FAB1");
    }

    @Test
    void getById_found() {
        when(fabRepository.findById(1L)).thenReturn(Optional.of(fab));

        Optional<Fab> result = service.getById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getLocation()).isEqualTo("Taiwan");
    }

    @Test
    void getById_notFound() {
        when(fabRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(service.getById(99L)).isEmpty();
    }

    @Test
    void getByFabCode_found() {
        when(fabRepository.findByFabCode("FAB1")).thenReturn(Optional.of(fab));

        Optional<Fab> result = service.getByFabCode("FAB1");

        assertThat(result).isPresent();
        assertThat(result.get().getFabCode()).isEqualTo("FAB1");
    }

    @Test
    void getByFabCode_notFound() {
        when(fabRepository.findByFabCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThat(service.getByFabCode("UNKNOWN")).isEmpty();
    }

    @Test
    void create_savesAndReturns() {
        when(fabRepository.save(any(Fab.class))).thenReturn(fab);

        Fab result = service.create(fab);

        assertThat(result.getFabCode()).isEqualTo("FAB1");
        verify(fabRepository, times(1)).save(fab);
    }

    @Test
    void update_found_updatesFields() {
        Fab updated = new Fab(null, "FAB2", "Japan");
        when(fabRepository.findById(1L)).thenReturn(Optional.of(fab));
        when(fabRepository.save(any(Fab.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<Fab> result = service.update(1L, updated);

        assertThat(result).isPresent();
        assertThat(result.get().getFabCode()).isEqualTo("FAB2");
        assertThat(result.get().getLocation()).isEqualTo("Japan");
    }

    @Test
    void update_notFound_returnsEmpty() {
        when(fabRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(service.update(99L, fab)).isEmpty();
        verify(fabRepository, never()).save(any());
    }

    @Test
    void delete_found_returnsTrue() {
        when(fabRepository.existsById(1L)).thenReturn(true);

        assertThat(service.delete(1L)).isTrue();
        verify(fabRepository).deleteById(1L);
    }

    @Test
    void delete_notFound_returnsFalse() {
        when(fabRepository.existsById(99L)).thenReturn(false);

        assertThat(service.delete(99L)).isFalse();
        verify(fabRepository, never()).deleteById(any());
    }
}
