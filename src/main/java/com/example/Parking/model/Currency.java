package com.example.Parking.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "currencies")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(length = 3)
    private String code;

    @Column(precision = 19, scale = 9)
    private BigDecimal rate;
}
