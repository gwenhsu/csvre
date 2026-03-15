package com.example.esgdp.csvreapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_card_re_preference",
        uniqueConstraints = @UniqueConstraint(columnNames = {"card_cust_code", "region", "bundle_type", "priority"}))
public class CustomerCardRePreference {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_cust_code", nullable = false)
    private CustomerCard customerCard;

    @NotNull
    @Column(nullable = false)
    private String region;

    @NotNull
    @Column(name = "bundle_type", nullable = false)
    private String bundleType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "energy_type", nullable = false)
    private EnergyType energyType;

    @NotNull
    @Column(nullable = false)
    private Integer priority;
}
