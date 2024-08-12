package com.ccs.desafiocaju.domain.components.impl;

import com.ccs.desafiocaju.domain.components.TransactionStrategy;
import com.ccs.desafiocaju.domain.infra.exceptions.CajuInsufficientBalanceException;
import com.ccs.desafiocaju.domain.models.entities.Transaction;
import com.ccs.desafiocaju.domain.models.enums.TransactionBalanceTypeEnum;
import com.ccs.desafiocaju.domain.models.enums.TransactionCodesEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class FoodTransactionStrategy implements TransactionStrategy {

    private final CashTransactionStrategy fallback;
    private static final Set<String> MCC_FOOD = Set.of("5411", "5412");

    @Override
    public TransactionCodesEnum processTransaction(Transaction transaction) {
        try {
            validarSaldo(transaction.getAccount().getBalanceFood(), transaction.getAmount());
        } catch (CajuInsufficientBalanceException e) {
            return fallback.processTransaction(transaction);
        }

        transaction.setTransactionBalanceType(TransactionBalanceTypeEnum.FOOD);
        transaction.getAccount()
                .setBalanceFood(transaction.getAccount()
                        .getBalanceFood()
                        .subtract(transaction.getAmount()));

        return TransactionCodesEnum.APROVADA;
    }

    @Override
    public Set<String> getMccs() {
        return MCC_FOOD;
    }

    @Override
    public Optional<TransactionStrategy> getFallback() {
        return Optional.of(fallback);
    }
}