package com.example.esgdp.csvreapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_card_plan_year",
        uniqueConstraints = @UniqueConstraint(columnNames = {"plan_id", "year"}))
public class CustomerCardPlanYear {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private CustomerCardPlan plan;

    @NotNull
    @Column(nullable = false)
    private Integer year;

    @Column(precision = 12, scale = 4)
    private BigDecimal rate;

    @Column(name = "is_show")
    private Boolean isShow;
}
