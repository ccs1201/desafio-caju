package com.ccs.desafiocaju.domain.components.impl;

import com.ccs.desafiocaju.domain.components.TransactionStrategy;
import com.ccs.desafiocaju.domain.models.entities.Transaction;
import com.ccs.desafiocaju.domain.models.enums.TransactionCodesEnum;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MealTransactionStrategy implements TransactionStrategy {

    private static final Set<String> MCC_MEAL = Set.of("5811", "5812");

    @Override
    public TransactionCodesEnum processTransaction(Transaction transaction) {
        validarSaldo(transaction);
        transaction.getAccount().setBalanceMeal(transaction.getAccount().getBalanceMeal().subtract(transaction.getAmount()));
        return TransactionCodesEnum.APROVADA;
    }

    @Override
    public Set<String> getMccs() {
        return MCC_MEAL;
    }
}