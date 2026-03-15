package com.example.esgdp.csvreapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_card")
public class CustomerCard {

    @Id
    @Column(name = "cust_code", nullable = false)
    private String custCode;

    @Column(name = "participate_since_year")
    private Integer participateSinceYear;

    @Column(name = "calender_since_month")
    private Integer calenderSinceMonth;

    @Column(name = "calender_to_month")
    private Integer calenderToMonth;
}
