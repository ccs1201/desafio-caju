package com.ccs.desafiocaju.domain.components.impl;

import com.ccs.desafiocaju.domain.infra.exceptions.CajuInsufficientBalanceException;
import com.ccs.desafiocaju.domain.models.entities.Account;
import com.ccs.desafiocaju.domain.models.entities.Transaction;
import com.ccs.desafiocaju.domain.models.enums.TransactionCodesEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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
    void testProcessTransaction_Approved() {
        transaction.setAmount(new BigDecimal("200.00"));

        TransactionCodesEnum result = strategy.processTransaction(transaction);

        assertEquals(TransactionCodesEnum.APROVADA, result);  // Transação aprovada
        assertEquals(new BigDecimal("100.00"), account.getBalanceCash());
    }

    @Test
    void testProcessTransaction_InsufficientFunds() {
        transaction.setAmount(new BigDecimal("350.00"));

        assertThrows(CajuInsufficientBalanceException.class, () -> strategy.processTransaction(transaction));

        assertEquals(new BigDecimal("300.00"), account.getBalanceCash());  // Saldo não deve ser alterado
    }

    @Test
    void getMccs() {
        assertEquals(Set.of("cash"), strategy.getMccs());
    }

    @Test
    void testGetFallback(){
        assertTrue(strategy.getFallback().isEmpty());
    }
}