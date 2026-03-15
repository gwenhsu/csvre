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
@Table(name = "emission_goal")
public class EmissionGoal {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true, nullable = false)
    private Integer year;

    @Column(precision = 12, scale = 4)
    private BigDecimal s1;

    @Column(name = "s2l", precision = 12, scale = 4)
    private BigDecimal s2L;

    @Column(name = "s2m", precision = 12, scale = 4)
    private BigDecimal s2M;

    @Column(precision = 12, scale = 4)
    private BigDecimal s3;
}
