package com.ccs.desafiocaju.domain.components.impl;

import com.ccs.desafiocaju.domain.components.TransactionStrategy;
import com.ccs.desafiocaju.domain.models.entities.Transaction;
import com.ccs.desafiocaju.domain.models.enums.TransactionCodesEnum;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CashTransactionStrategy implements TransactionStrategy {

    private static final String CASH_TRANSACTION = "cash";
    private static final Set<String> MCC_CASH = Set.of(CASH_TRANSACTION);

    @Override
    public TransactionCodesEnum processTransaction(Transaction transaction) {
        validarSaldo(transaction);

        transaction.getAccount().setBalanceCash(transaction.getAccount().getBalanceCash().subtract(transaction.getAmount()));
        return TransactionCodesEnum.APROVADA;

    }

    @Override
    public Set<String> getMccs() {
        return MCC_CASH;
    }
}