package com.ccs.desafiocaju.domain.models.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DynamicUpdate
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal balanceFood;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal balanceMeal;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal balanceCash;

    @OneToMany(mappedBy = "account", orphanRemoval = true)
    private List<Transaction> transactions;

}