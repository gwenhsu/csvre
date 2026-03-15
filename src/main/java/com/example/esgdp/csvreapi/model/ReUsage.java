package com.example.esgdp.csvreapi.model;

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
@Table(name = "re_usage",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fab_id", "year", "month"}))
public class ReUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fab_id", nullable = false)
    private Fab fab;

    @NotNull
    @Column(nullable = false)
    private Integer year;

    @NotNull
    @Column(nullable = false)
    private Integer month;

    @Column(precision = 12, scale = 4)
    private BigDecimal usage;
}
