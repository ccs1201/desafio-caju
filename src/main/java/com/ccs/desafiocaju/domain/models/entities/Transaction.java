package com.ccs.desafiocaju.domain.models.entities;

import com.ccs.desafiocaju.domain.models.enums.TransactionBalanceTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @ManyToOne(optional = false)
    private Merchant merchant;

    @Column(nullable = false)
    private String mcc;

    @Enumerated(EnumType.STRING)
    private TransactionBalanceTypeEnum transactionBalanceType;

}
