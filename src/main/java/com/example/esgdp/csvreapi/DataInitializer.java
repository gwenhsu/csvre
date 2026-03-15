package com.example.esgdp.csvreapi;

import com.example.esgdp.csvreapi.model.*;
import com.example.esgdp.csvreapi.repository.*;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    ApplicationRunner initData(
            FabRepository fabRepo,
            PowerConsumptionRepository powerRepo,
            ReUsageRepository reUsageRepo,
            EmissionRepository emissionRepo,
            EmissionGoalRepository emissionGoalRepo,
            PowerFactorRepository powerFactorRepo,
            ReGoalRepository reGoalRepo,
            ReProcurementRepository reProcurementRepo,
            CustomerCardRepository customerCardRepo,
            CustomerCardThirdPartyRepository thirdPartyRepo,
            CustomerCardParticipateOptionRepository participateOptionRepo,
            CustomerCardRePreferenceRepository rePreferenceRepo,
            CustomerCardPlanRepository planRepo,
            CustomerCardPlanYearRepository planYearRepo
    ) {
        return args -> {
            if (fabRepo.count() > 0) return; // 避免重複插入

            // fab
            Fab fabA = fabRepo.save(new Fab(null, "FAB-A", "Taiwan"));
            Fab fabB = fabRepo.save(new Fab(null, "FAB-B", "Japan"));
            Fab fabC = fabRepo.save(new Fab(null, "FAB-C", "USA"));

            // power_consumption
            powerRepo.saveAll(List.of(
                new PowerConsumption(null, fabA, 2024, 1, new BigDecimal("1234.5678")),
                new PowerConsumption(null, fabA, 2024, 2, new BigDecimal("1301.2345")),
                new PowerConsumption(null, fabB, 2024, 1, new BigDecimal("987.6543")),
                new PowerConsumption(null, fabB, 2024, 2, new BigDecimal("1045.3210")),
                new PowerConsumption(null, fabC, 2024, 1, new BigDecimal("2100.0000")),
                new PowerConsumption(null, fabC, 2024, 2, new BigDecimal("2250.7890"))
            ));

            // re_usage
            reUsageRepo.saveAll(List.of(
                new ReUsage(null, fabA, 2024, 1, new BigDecimal("300.1234")),
                new ReUsage(null, fabA, 2024, 2, new BigDecimal("320.5678")),
                new ReUsage(null, fabB, 2024, 1, new BigDecimal("200.0000")),
                new ReUsage(null, fabB, 2024, 2, new BigDecimal("215.4321")),
                new ReUsage(null, fabC, 2024, 1, new BigDecimal("500.9999")),
                new ReUsage(null, fabC, 2024, 2, new BigDecimal("530.1111"))
            ));

            // emission
            emissionRepo.saveAll(List.of(
                new Emission(null, fabA, 2024, 1, new BigDecimal("100.1111"), new BigDecimal("200.2222"), new BigDecimal("150.3333"), new BigDecimal("50.4444")),
                new Emission(null, fabA, 2024, 2, new BigDecimal("105.5555"), new BigDecimal("210.6666"), new BigDecimal("155.7777"), new BigDecimal("52.8888")),
                new Emission(null, fabB, 2024, 1, new BigDecimal("80.0001"),  new BigDecimal("160.0002"), new BigDecimal("120.0003"), new BigDecimal("40.0004")),
                new Emission(null, fabB, 2024, 2, new BigDecimal("83.1234"),  new BigDecimal("166.2345"), new BigDecimal("124.3456"), new BigDecimal("41.4567")),
                new Emission(null, fabC, 2024, 1, new BigDecimal("200.0000"), new BigDecimal("400.0000"), new BigDecimal("300.0000"), new BigDecimal("100.0000")),
                new Emission(null, fabC, 2024, 2, new BigDecimal("210.5000"), new BigDecimal("421.0000"), new BigDecimal("315.7500"), new BigDecimal("105.2500"))
            ));

            // emission_goal
            emissionGoalRepo.saveAll(List.of(
                new EmissionGoal(null, 2024, new BigDecimal("900.0000"),  new BigDecimal("1800.0000"), new BigDecimal("1350.0000"), new BigDecimal("450.0000")),
                new EmissionGoal(null, 2025, new BigDecimal("800.0000"),  new BigDecimal("1600.0000"), new BigDecimal("1200.0000"), new BigDecimal("400.0000")),
                new EmissionGoal(null, 2026, new BigDecimal("700.0000"),  new BigDecimal("1400.0000"), new BigDecimal("1050.0000"), new BigDecimal("350.0000"))
            ));

            // power_factor
            powerFactorRepo.saveAll(List.of(
                new PowerFactor(null, fabA, 2024, new BigDecimal("0.9500")),
                new PowerFactor(null, fabA, 2025, new BigDecimal("0.9600")),
                new PowerFactor(null, fabB, 2024, new BigDecimal("0.9300")),
                new PowerFactor(null, fabB, 2025, new BigDecimal("0.9400")),
                new PowerFactor(null, fabC, 2024, new BigDecimal("0.9800")),
                new PowerFactor(null, fabC, 2025, new BigDecimal("0.9850"))
            ));

            // re_goal
            reGoalRepo.saveAll(List.of(
                new ReGoal(null, 2024, new BigDecimal("0.3000")),
                new ReGoal(null, 2025, new BigDecimal("0.4000")),
                new ReGoal(null, 2026, new BigDecimal("0.5000"))
            ));

            // re_procurement
            reProcurementRepo.saveAll(List.of(
                new ReProcurement(null, "Asia",   2024, 1,  0, EnergyType.WIND,  new BigDecimal("500.1234")),
                new ReProcurement(null, "Asia",   2024, 1,  0, EnergyType.SOLAR, new BigDecimal("300.5678")),
                new ReProcurement(null, "Asia",   2024, 2,  1, EnergyType.HYDRO, new BigDecimal("150.9999")),
                new ReProcurement(null, "Europe", 2024, 1,  0, EnergyType.WIND,  new BigDecimal("800.0000")),
                new ReProcurement(null, "Europe", 2024, 1,  0, EnergyType.BIO,   new BigDecimal("200.1111")),
                new ReProcurement(null, "Europe", 2024, 2, 12, EnergyType.SOLAR, new BigDecimal("450.2222")),
                new ReProcurement(null, "USA",    2024, 1,  5, EnergyType.WIND,  new BigDecimal("1000.0000")),
                new ReProcurement(null, "USA",    2024, 2,  0, EnergyType.SOLAR, new BigDecimal("600.3333"))
            ));

            // customer_card
            CustomerCard cardC001 = customerCardRepo.save(new CustomerCard("C001", 2022, 1, 12));
            CustomerCard cardC002 = customerCardRepo.save(new CustomerCard("C002", 2023, 3, 12));
            CustomerCard cardC003 = customerCardRepo.save(new CustomerCard("C003", 2021, 6, 12));

            // third parties
            thirdPartyRepo.save(new CustomerCardThirdParty(null, cardC001, DirectionType.TO,   "C002", new BigDecimal("3.1234")));
            thirdPartyRepo.save(new CustomerCardThirdParty(null, cardC001, DirectionType.TO,   "C003", new BigDecimal("2.2340")));
            thirdPartyRepo.save(new CustomerCardThirdParty(null, cardC001, DirectionType.FROM, "C002", new BigDecimal("1.5000")));
            thirdPartyRepo.save(new CustomerCardThirdParty(null, cardC002, DirectionType.TO,   "C003", new BigDecimal("4.0000")));

            // participate options
            participateOptionRepo.save(new CustomerCardParticipateOption(null, cardC001, 1));
            participateOptionRepo.save(new CustomerCardParticipateOption(null, cardC001, 2));
            participateOptionRepo.save(new CustomerCardParticipateOption(null, cardC001, 3));
            participateOptionRepo.save(new CustomerCardParticipateOption(null, cardC002, 1));

            // re preference
            // C001 - TW
            rePreferenceRepo.save(new CustomerCardRePreference(null, cardC001, "TW", "Bundle",   EnergyType.WIND,  1));
            rePreferenceRepo.save(new CustomerCardRePreference(null, cardC001, "TW", "Bundle",   EnergyType.SOLAR, 2));
            rePreferenceRepo.save(new CustomerCardRePreference(null, cardC001, "TW", "Bundle",   EnergyType.HYDRO, 3));
            rePreferenceRepo.save(new CustomerCardRePreference(null, cardC001, "TW", "Unbundle", EnergyType.WIND,  1));
            rePreferenceRepo.save(new CustomerCardRePreference(null, cardC001, "TW", "Unbundle", EnergyType.SOLAR, 2));
            rePreferenceRepo.save(new CustomerCardRePreference(null, cardC001, "TW", "Unbundle", EnergyType.HYDRO, 3));
            // C001 - JP
            rePreferenceRepo.save(new CustomerCardRePreference(null, cardC001, "JP", "Bundle",   EnergyType.WIND,  1));
            rePreferenceRepo.save(new CustomerCardRePreference(null, cardC001, "JP", "Bundle",   EnergyType.HYDRO, 2));
            rePreferenceRepo.save(new CustomerCardRePreference(null, cardC001, "JP", "Unbundle", EnergyType.SOLAR, 1));
            rePreferenceRepo.save(new CustomerCardRePreference(null, cardC001, "JP", "Unbundle", EnergyType.WIND,  2));
            // C002 - TW
            rePreferenceRepo.save(new CustomerCardRePreference(null, cardC002, "TW", "Bundle",   EnergyType.SOLAR, 1));
            rePreferenceRepo.save(new CustomerCardRePreference(null, cardC002, "TW", "Bundle",   EnergyType.WIND,  2));
            rePreferenceRepo.save(new CustomerCardRePreference(null, cardC002, "TW", "Unbundle", EnergyType.HYDRO, 1));
            rePreferenceRepo.save(new CustomerCardRePreference(null, cardC002, "TW", "Unbundle", EnergyType.SOLAR, 2));

            // plan: expected
            CustomerCardPlan c001Expected = planRepo.save(new CustomerCardPlan(null, cardC001, PlanType.EXPECTED, 2));
            planYearRepo.save(new CustomerCardPlanYear(null, c001Expected, 2024, new BigDecimal("0.3000"), true));
            planYearRepo.save(new CustomerCardPlanYear(null, c001Expected, 2025, new BigDecimal("0.2000"), false));

            // plan: commitment
            CustomerCardPlan c001Commitment = planRepo.save(new CustomerCardPlan(null, cardC001, PlanType.COMMITMENT, 1));
            planYearRepo.save(new CustomerCardPlanYear(null, c001Commitment, 2024, new BigDecimal("0.3000"), true));
            planYearRepo.save(new CustomerCardPlanYear(null, c001Commitment, 2025, new BigDecimal("0.2000"), false));

            CustomerCardPlan c002Expected = planRepo.save(new CustomerCardPlan(null, cardC002, PlanType.EXPECTED, 1));
            planYearRepo.save(new CustomerCardPlanYear(null, c002Expected, 2024, new BigDecimal("0.4000"), true));
        };
    }
}
