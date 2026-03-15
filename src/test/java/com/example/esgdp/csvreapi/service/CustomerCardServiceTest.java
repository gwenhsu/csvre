package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.dto.*;
import com.example.esgdp.csvreapi.model.*;
import com.example.esgdp.csvreapi.repository.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerCardServiceTest {

    @Mock private CustomerCardRepository cardRepo;
    @Mock private CustomerCardThirdPartyRepository thirdPartyRepo;
    @Mock private CustomerCardParticipateOptionRepository optionRepo;
    @Mock private CustomerCardRePreferenceRepository rePreferenceRepo;
    @Mock private CustomerCardPlanRepository planRepo;
    @Mock private CustomerCardPlanYearRepository planYearRepo;
    @Mock private CustomerService customerService;
    @Mock private EntityManager entityManager;

    @InjectMocks
    private CustomerCardService service;

    private CustomerCard card;
    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        card = new CustomerCard("C001", 2022, 1, 12);

        customerDto = new CustomerDto();
        customerDto.setCustCode("C001");
        customerDto.setCustShortname("TSMC");
        customerDto.setCustRegion("Asia");
    }

    private void stubCustomerService() {
        when(customerService.getAll()).thenReturn(List.of(customerDto));
        when(thirdPartyRepo.findByCustomerCardCustCodeAndDirection("C001", DirectionType.TO)).thenReturn(List.of());
        when(thirdPartyRepo.findByCustomerCardCustCodeAndDirection("C001", DirectionType.FROM)).thenReturn(List.of());
        when(optionRepo.findByCustomerCardCustCode("C001")).thenReturn(List.of());
        when(rePreferenceRepo.findByCustomerCardCustCode("C001")).thenReturn(List.of());
        when(planRepo.findByCustomerCardCustCodeAndPlanType("C001", PlanType.EXPECTED)).thenReturn(Optional.empty());
        when(planRepo.findByCustomerCardCustCodeAndPlanType("C001", PlanType.COMMITMENT)).thenReturn(Optional.empty());
    }

    @Test
    void getAll_returnsAllCards() {
        when(cardRepo.findAll()).thenReturn(List.of(card));
        stubCustomerService();

        List<CustomerCardDto> result = service.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCustCode()).isEqualTo("C001");
        assertThat(result.get(0).getCustShortname()).isEqualTo("TSMC");
    }

    @Test
    void getByCustCode_found() {
        when(cardRepo.findById("C001")).thenReturn(Optional.of(card));
        stubCustomerService();

        Optional<CustomerCardDto> result = service.getByCustCode("C001");

        assertThat(result).isPresent();
        assertThat(result.get().getCustCode()).isEqualTo("C001");
    }

    @Test
    void getByCustCode_notFound() {
        when(cardRepo.findById("UNKNOWN")).thenReturn(Optional.empty());

        Optional<CustomerCardDto> result = service.getByCustCode("UNKNOWN");

        assertThat(result).isEmpty();
    }

    @Test
    void create_savesCardAndSubEntities() {
        CustomerCardDto dto = buildDto();
        when(cardRepo.save(any(CustomerCard.class))).thenReturn(card);
        stubCustomerService();

        CustomerCardDto result = service.create(dto);

        assertThat(result.getCustCode()).isEqualTo("C001");
        verify(cardRepo, times(1)).save(any(CustomerCard.class));
    }

    @Test
    void update_found_updatesAndReturns() {
        CustomerCardDto dto = buildDto();
        dto.setParticipateSinceYear(2023);

        when(cardRepo.findById("C001")).thenReturn(Optional.of(card));
        when(cardRepo.save(any(CustomerCard.class))).thenReturn(card);
        when(planRepo.findByCustomerCardCustCode("C001")).thenReturn(List.of());
        stubCustomerService();

        Optional<CustomerCardDto> result = service.update("C001", dto);

        assertThat(result).isPresent();
        verify(thirdPartyRepo).deleteByCustomerCardCustCode("C001");
        verify(optionRepo).deleteByCustomerCardCustCode("C001");
        verify(rePreferenceRepo).deleteByCustomerCardCustCode("C001");
        verify(planRepo).deleteByCustomerCardCustCode("C001");
        verify(entityManager).flush();
    }

    @Test
    void update_notFound_returnsEmpty() {
        when(cardRepo.findById("UNKNOWN")).thenReturn(Optional.empty());

        Optional<CustomerCardDto> result = service.update("UNKNOWN", buildDto());

        assertThat(result).isEmpty();
        verify(cardRepo, never()).save(any());
    }

    @Test
    void getAll_enrichesThirdPartyFromCustomerService() {
        CustomerCardThirdParty tp = new CustomerCardThirdParty();
        tp.setThirdPartyCustCode("C002");
        tp.setDirection(DirectionType.TO);
        tp.setRevenue(new BigDecimal("500000"));
        tp.setCustomerCard(card);

        CustomerDto tpCustomer = new CustomerDto();
        tpCustomer.setCustCode("C002");
        tpCustomer.setCustShortname("Samsung");
        tpCustomer.setCustRegion("Asia");

        when(cardRepo.findAll()).thenReturn(List.of(card));
        when(customerService.getAll()).thenReturn(List.of(customerDto, tpCustomer));
        when(thirdPartyRepo.findByCustomerCardCustCodeAndDirection("C001", DirectionType.TO)).thenReturn(List.of(tp));
        when(thirdPartyRepo.findByCustomerCardCustCodeAndDirection("C001", DirectionType.FROM)).thenReturn(List.of());
        when(optionRepo.findByCustomerCardCustCode("C001")).thenReturn(List.of());
        when(rePreferenceRepo.findByCustomerCardCustCode("C001")).thenReturn(List.of());
        when(planRepo.findByCustomerCardCustCodeAndPlanType("C001", PlanType.EXPECTED)).thenReturn(Optional.empty());
        when(planRepo.findByCustomerCardCustCodeAndPlanType("C001", PlanType.COMMITMENT)).thenReturn(Optional.empty());

        List<CustomerCardDto> result = service.getAll();

        assertThat(result.get(0).getToThirdParties()).hasSize(1);
        assertThat(result.get(0).getToThirdParties().get(0).getCustShortname()).isEqualTo("Samsung");
    }

    @Test
    void create_withRePreference_savesCorrectly() {
        CustomerCardDto dto = buildDto();
        RePreferenceItemDto item = new RePreferenceItemDto();
        item.setType(EnergyType.WIND);
        item.setPriority(1);
        dto.setRePreference(Map.of("TW", Map.of("UNBUNDLE", List.of(item))));

        when(cardRepo.save(any(CustomerCard.class))).thenReturn(card);
        stubCustomerService();

        service.create(dto);

        verify(rePreferenceRepo, times(1)).save(any(CustomerCardRePreference.class));
    }

    private CustomerCardDto buildDto() {
        CustomerCardDto dto = new CustomerCardDto();
        dto.setCustCode("C001");
        dto.setParticipateSinceYear(2022);
        dto.setCalenderSinceMonth(1);
        dto.setCalenderToMonth(12);
        return dto;
    }
}
