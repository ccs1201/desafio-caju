package com.ccs.desafiocaju.domain.components.impl;

import com.ccs.desafiocaju.domain.infra.exceptions.CajuInsufficientBalanceException;
import com.ccs.desafiocaju.domain.models.entities.Account;
import com.ccs.desafiocaju.domain.models.entities.Transaction;
import com.ccs.desafiocaju.domain.models.enums.TransactionCodesEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class MealTransactionStrategyTest {

    @InjectMocks
    private MealTransactionStrategy strategy;
    private Account account;
    private Transaction transaction;

    @BeforeEach
     void setUp() {
        account = new Account();
        account.setBalanceMeal(new BigDecimal("200.00"));
        transaction = new Transaction();
        transaction.setAccount(account);
    }

    @ParameterizedTest
    @ValueSource(strings = {"5811", "5812"})
    void getMccs(String mcc) {
        assertTrue(strategy.getMccs().contains(mcc));
    }

    @Test
     void testProcessTransactionAprovda() {
        transaction.setAmount(new BigDecimal("100.00"));

        var code = strategy.processTransaction(transaction);

        assertEquals(TransactionCodesEnum.APROVADA, code);  // Transação aprovada
        assertEquals(new BigDecimal("100.00"), account.getBalanceMeal());
    }

    @Test
     void testProcessTransactionSaldoInsuficiente() {
        transaction.setAmount(new BigDecimal("250.00"));

        assertThrows(CajuInsufficientBalanceException.class, () -> strategy.processTransaction(transaction));

        assertEquals(new BigDecimal("200.00"), account.getBalanceMeal());
    }
}