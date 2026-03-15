package com.example.esgdp.csvreapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "re_procurement",
        uniqueConstraints = @UniqueConstraint(columnNames = {"region", "year", "month", "energy_type"}))
public class ReProcurement {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String region;

    @NotNull
    @Column(nullable = false)
    private Integer year;

    @NotNull
    @Column(nullable = false)
    private Integer month;

    @Column(name = "bundle_type")
    private Integer bundleType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "energy_type", nullable = false)
    private EnergyType energyType;

    @Column(precision = 12, scale = 4)
    private BigDecimal procurement;
}
