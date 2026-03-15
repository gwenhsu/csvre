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
@Table(name = "customer_card_third_party",
        uniqueConstraints = @UniqueConstraint(columnNames = {"card_cust_code", "direction", "third_party_cust_code"}))
public class CustomerCardThirdParty {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_cust_code", nullable = false)
    private CustomerCard customerCard;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DirectionType direction;

    @NotNull
    @Column(name = "third_party_cust_code", nullable = false)
    private String thirdPartyCustCode;

    @Column(precision = 12, scale = 4)
    private BigDecimal revenue;
}
