package com.ccs.desafiocaju.domain.components.impl;

import com.ccs.desafiocaju.domain.components.TransactionStrategy;
import com.ccs.desafiocaju.domain.models.entities.Transaction;
import com.ccs.desafiocaju.domain.models.enums.TransactionCodesEnum;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FoodTransactionStrategy implements TransactionStrategy {

    private static final Set<String> MCC_FOOD = Set.of("5411", "5412");

    @Override
    public TransactionCodesEnum processTransaction(Transaction transaction) {
        validarSaldo(transaction);
        transaction.getAccount().setBalanceFood(transaction.getAccount().getBalanceFood().subtract(transaction.getAmount()));
        return TransactionCodesEnum.APROVADA;

    }

    @Override
    public Set<String> getMccs() {
        return MCC_FOOD;
    }
}