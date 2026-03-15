package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.dto.*;
import com.example.esgdp.csvreapi.model.*;
import com.example.esgdp.csvreapi.repository.*;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerCardService {

    private final CustomerCardRepository cardRepo;
    private final CustomerCardThirdPartyRepository thirdPartyRepo;
    private final CustomerCardParticipateOptionRepository optionRepo;
    private final CustomerCardRePreferenceRepository rePreferenceRepo;
    private final CustomerCardPlanRepository planRepo;
    private final CustomerCardPlanYearRepository planYearRepo;
    private final CustomerService customerService;
    private final EntityManager entityManager;

    public CustomerCardService(
            CustomerCardRepository cardRepo,
            CustomerCardThirdPartyRepository thirdPartyRepo,
            CustomerCardParticipateOptionRepository optionRepo,
            CustomerCardRePreferenceRepository rePreferenceRepo,
            CustomerCardPlanRepository planRepo,
            CustomerCardPlanYearRepository planYearRepo,
            CustomerService customerService,
            EntityManager entityManager) {
        this.cardRepo = cardRepo;
        this.thirdPartyRepo = thirdPartyRepo;
        this.optionRepo = optionRepo;
        this.rePreferenceRepo = rePreferenceRepo;
        this.planRepo = planRepo;
        this.planYearRepo = planYearRepo;
        this.customerService = customerService;
        this.entityManager = entityManager;
    }

    public List<CustomerCardDto> getAll() {
        log.info("GET all customer cards");
        List<CustomerCardDto> result = cardRepo.findAll().stream().map(this::toDto).toList();
        log.info("Found {} customer card(s)", result.size());
        return result;
    }

    public Optional<CustomerCardDto> getByCustCode(String custCode) {
        log.info("GET customer card: custCode={}", custCode);
        Optional<CustomerCardDto> result = cardRepo.findById(custCode).map(this::toDto);
        if (result.isEmpty()) {
            log.warn("Customer card not found: custCode={}", custCode);
        }
        return result;
    }

    @Transactional
    public CustomerCardDto create(CustomerCardDto dto) {
        log.info("POST customer card: custCode={}", dto.getCustCode());
        CustomerCard card = new CustomerCard(
                dto.getCustCode(),
                dto.getParticipateSinceYear(),
                dto.getCalenderSinceMonth(),
                dto.getCalenderToMonth()
        );
        cardRepo.save(card);
        saveSubEntities(card, dto);
        log.info("Customer card created: custCode={}", card.getCustCode());
        return toDto(card);
    }

    @Transactional
    public Optional<CustomerCardDto> update(String custCode, CustomerCardDto dto) {
        log.info("PUT customer card: custCode={}", custCode);
        return cardRepo.findById(custCode).map(card -> {
            card.setParticipateSinceYear(dto.getParticipateSinceYear());
            card.setCalenderSinceMonth(dto.getCalenderSinceMonth());
            card.setCalenderToMonth(dto.getCalenderToMonth());
            cardRepo.save(card);

            log.debug("Deleting sub-entities for custCode={}", custCode);
            thirdPartyRepo.deleteByCustomerCardCustCode(custCode);
            optionRepo.deleteByCustomerCardCustCode(custCode);
            rePreferenceRepo.deleteByCustomerCardCustCode(custCode);
            planRepo.findByCustomerCardCustCode(custCode).forEach(p -> {
                planYearRepo.deleteByPlanId(p.getId());
            });
            planRepo.deleteByCustomerCardCustCode(custCode);
            entityManager.flush();

            saveSubEntities(card, dto);
            log.info("Customer card updated: custCode={}", custCode);
            return toDto(card);
        });
    }

    // ── 私有方法 ──────────────────────────────────────────

    private void saveSubEntities(CustomerCard card, CustomerCardDto dto) {
        String custCode = card.getCustCode();

        if (dto.getToThirdParties() != null) {
            log.debug("Saving {} toThirdParties for custCode={}", dto.getToThirdParties().size(), custCode);
            dto.getToThirdParties().forEach(t -> {
                CustomerCardThirdParty entity = new CustomerCardThirdParty();
                entity.setCustomerCard(card);
                entity.setDirection(DirectionType.TO);
                entity.setThirdPartyCustCode(t.getCustCode());
                entity.setRevenue(t.getRevenue());
                thirdPartyRepo.save(entity);
            });
        }

        if (dto.getFromThirdParties() != null) {
            log.debug("Saving {} fromThirdParties for custCode={}", dto.getFromThirdParties().size(), custCode);
            dto.getFromThirdParties().forEach(t -> {
                CustomerCardThirdParty entity = new CustomerCardThirdParty();
                entity.setCustomerCard(card);
                entity.setDirection(DirectionType.FROM);
                entity.setThirdPartyCustCode(t.getCustCode());
                entity.setRevenue(t.getRevenue());
                thirdPartyRepo.save(entity);
            });
        }

        if (dto.getParticipateOptions() != null) {
            log.debug("Saving {} participateOptions for custCode={}", dto.getParticipateOptions().size(), custCode);
            dto.getParticipateOptions().forEach(opt -> {
                CustomerCardParticipateOption entity = new CustomerCardParticipateOption();
                entity.setCustomerCard(card);
                entity.setOptionValue(opt);
                optionRepo.save(entity);
            });
        }

        if (dto.getRePreference() != null) {
            log.debug("Saving rePreference for custCode={}", custCode);
            dto.getRePreference().forEach((region, bundleMap) ->
                bundleMap.forEach((bundleType, items) ->
                    items.forEach(item -> {
                        CustomerCardRePreference entity = new CustomerCardRePreference();
                        entity.setCustomerCard(card);
                        entity.setRegion(region);
                        entity.setBundleType(bundleType);
                        entity.setEnergyType(item.getType());
                        entity.setPriority(item.getPriority());
                        rePreferenceRepo.save(entity);
                    })
                )
            );
        }

        savePlan(card, PlanType.EXPECTED, dto.getExpected());
        savePlan(card, PlanType.COMMITMENT, dto.getCommitment());
    }

    private void savePlan(CustomerCard card, PlanType planType, PlanDto planDto) {
        if (planDto == null) {
            log.debug("No {} plan for custCode={}", planType, card.getCustCode());
            return;
        }
        log.debug("Saving {} plan for custCode={}", planType, card.getCustCode());
        CustomerCardPlan plan = new CustomerCardPlan();
        plan.setCustomerCard(card);
        plan.setPlanType(planType);
        plan.setOptionValue(planDto.getOption());
        planRepo.save(plan);

        if (planDto.getYears() != null) {
            planDto.getYears().forEach(y -> {
                CustomerCardPlanYear year = new CustomerCardPlanYear();
                year.setPlan(plan);
                year.setYear(y.getYear());
                year.setRate(y.getRate());
                year.setIsShow(y.getIsShow());
                planYearRepo.save(year);
            });
        }
    }

    private CustomerCardDto toDto(CustomerCard card) {
        log.debug("Building DTO for custCode={}", card.getCustCode());

        Map<String, CustomerDto> customerMap = customerService.getAll().stream()
                .collect(Collectors.toMap(CustomerDto::getCustCode, c -> c));

        CustomerCardDto dto = new CustomerCardDto();
        dto.setCustCode(card.getCustCode());
        CustomerDto custInfo = customerMap.get(card.getCustCode());
        if (custInfo != null) {
            dto.setCustShortname(custInfo.getCustShortname());
            dto.setCustRegion(custInfo.getCustRegion());
        } else {
            log.warn("Customer info not found from external API: custCode={}", card.getCustCode());
        }
        dto.setParticipateSinceYear(card.getParticipateSinceYear());
        dto.setCalenderSinceMonth(card.getCalenderSinceMonth());
        dto.setCalenderToMonth(card.getCalenderToMonth());

        dto.setToThirdParties(buildThirdPartyDtos(
                thirdPartyRepo.findByCustomerCardCustCodeAndDirection(card.getCustCode(), DirectionType.TO),
                customerMap));

        dto.setFromThirdParties(buildThirdPartyDtos(
                thirdPartyRepo.findByCustomerCardCustCodeAndDirection(card.getCustCode(), DirectionType.FROM),
                customerMap));

        dto.setParticipateOptions(
                optionRepo.findByCustomerCardCustCode(card.getCustCode()).stream()
                        .map(CustomerCardParticipateOption::getOptionValue)
                        .toList());

        Map<String, Map<String, List<RePreferenceItemDto>>> rePreference = new LinkedHashMap<>();
        rePreferenceRepo.findByCustomerCardCustCode(card.getCustCode()).forEach(r -> {
            rePreference
                .computeIfAbsent(r.getRegion(), k -> new LinkedHashMap<>())
                .computeIfAbsent(r.getBundleType(), k -> new ArrayList<>())
                .add(buildRePreferenceItem(r));
        });
        dto.setRePreference(rePreference);

        dto.setExpected(buildPlanDto(card.getCustCode(), PlanType.EXPECTED));
        dto.setCommitment(buildPlanDto(card.getCustCode(), PlanType.COMMITMENT));

        return dto;
    }

    private List<ThirdPartyDto> buildThirdPartyDtos(
            List<CustomerCardThirdParty> entities,
            Map<String, CustomerDto> customerMap) {
        return entities.stream().map(t -> {
            ThirdPartyDto d = new ThirdPartyDto();
            d.setCustCode(t.getThirdPartyCustCode());
            CustomerDto info = customerMap.get(t.getThirdPartyCustCode());
            if (info != null) {
                d.setCustShortname(info.getCustShortname());
                d.setCustRegion(info.getCustRegion());
            } else {
                log.warn("Third party customer info not found from external API: custCode={}", t.getThirdPartyCustCode());
            }
            d.setRevenue(t.getRevenue());
            return d;
        }).toList();
    }

    private RePreferenceItemDto buildRePreferenceItem(CustomerCardRePreference r) {
        RePreferenceItemDto item = new RePreferenceItemDto();
        item.setType(r.getEnergyType());
        item.setPriority(r.getPriority());
        return item;
    }

    private PlanDto buildPlanDto(String custCode, PlanType planType) {
        return planRepo.findByCustomerCardCustCodeAndPlanType(custCode, planType).map(plan -> {
            PlanDto planDto = new PlanDto();
            planDto.setOption(plan.getOptionValue());
            planDto.setYears(planYearRepo.findByPlanId(plan.getId()).stream().map(y -> {
                PlanYearDto yearDto = new PlanYearDto();
                yearDto.setYear(y.getYear());
                yearDto.setRate(y.getRate());
                yearDto.setIsShow(y.getIsShow());
                return yearDto;
            }).toList());
            return planDto;
        }).orElse(null);
    }
}
