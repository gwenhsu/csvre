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
@Table(name = "customer_card_participate_option",
        uniqueConstraints = @UniqueConstraint(columnNames = {"card_cust_code", "option_value"}))
public class CustomerCardParticipateOption {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_cust_code", nullable = false)
    private CustomerCard customerCard;

    @NotNull
    @Column(name = "option_value", nullable = false)
    private Integer optionValue;
}
