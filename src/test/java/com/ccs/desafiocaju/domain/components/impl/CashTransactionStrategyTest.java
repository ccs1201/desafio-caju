package com.ccs.desafiocaju.domain.components.impl;

import com.ccs.desafiocaju.domain.models.entities.Account;
import com.ccs.desafiocaju.domain.models.entities.Transaction;
import com.ccs.desafiocaju.domain.models.enums.TransactionBalanceTypeEnum;
import com.ccs.desafiocaju.domain.models.enums.TransactionCodesEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CashTransactionStrategyTest {

    private CashTransactionStrategy strategy;
    private Transaction transaction;
    private Account account;

    @BeforeEach
    void setUp() {
        strategy = new CashTransactionStrategy();
        account = new Account();
        account.setBalanceCash(new BigDecimal("300.00"));

        transaction = new Transaction();
        transaction.setAccount(account);
    }

    @Test
    void testProcessTransactionAprovada() {
        transaction.setAmount(new BigDecimal("200.00"));

        TransactionCodesEnum result = strategy.processTransaction(transaction);

        assertEquals(TransactionCodesEnum.APROVADA, result);
        assertEquals(new BigDecimal("100.00"), account.getBalanceCash());
        assertEquals(TransactionBalanceTypeEnum.CASH, transaction.getTransactionBalanceType());
    }

    @Test
    void testProcessTransactionSaldoInsuficiente() {
        transaction.setAmount(new BigDecimal("350.00"));

        var result = strategy.processTransaction(transaction);

        assertEquals(new BigDecimal("300.00"), account.getBalanceCash());
        assertEquals(TransactionCodesEnum.SALDO_INSUFICIENTE, result);
    }

    @Test
    void getMccs() {
        assertEquals(Set.of("cash"), strategy.getMccs());
    }

    @Test
    void testGetFallback() {
        assertTrue(strategy.getFallback().isEmpty());
    }
}