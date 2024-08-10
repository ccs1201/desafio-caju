package com.ccs.desafiocaju.domain.components;

import com.ccs.desafiocaju.domain.infra.exceptions.CajuInsufficientBalanceException;
import com.ccs.desafiocaju.domain.models.entities.Transaction;
import com.ccs.desafiocaju.domain.models.enums.TransactionCodesEnum;

import java.util.Set;

public interface TransactionStrategy {

    default void validarSaldo(Transaction transaction) {
        if (transaction.getAccount().getBalanceCash().compareTo(transaction.getAmount()) < 0)
            throw new CajuInsufficientBalanceException(this.getClass().getSimpleName());
    }

    TransactionCodesEnum processTransaction(Transaction transactio);

    Set<String> getMccs();
}
