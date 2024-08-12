package com.ccs.desafiocaju.domain.components.impl;

import com.ccs.desafiocaju.domain.models.entities.Account;
import com.ccs.desafiocaju.domain.models.entities.Transaction;
import com.ccs.desafiocaju.domain.models.enums.TransactionBalanceTypeEnum;
import com.ccs.desafiocaju.domain.models.enums.TransactionCodesEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealTransactionStrategyTest {

    @InjectMocks
    private MealTransactionStrategy strategy;
    @Mock
    private CashTransactionStrategy fallback;
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

        assertEquals(TransactionCodesEnum.APROVADA, code);
        assertEquals(new BigDecimal("100.00"), account.getBalanceMeal());
        assertEquals(TransactionBalanceTypeEnum.MEAL, transaction.getTransactionBalanceType());
        verify(fallback, never()).processTransaction(transaction);
    }

    @Test
    void testProcessTransactionSaldoInsuficiente() {
        when(fallback.processTransaction(transaction)).thenReturn(TransactionCodesEnum.SALDO_INSUFICIENTE);

        transaction.setAmount(new BigDecimal("250.00"));

        var result = strategy.processTransaction(transaction);

        assertEquals(new BigDecimal("200.00"), account.getBalanceMeal());
        assertEquals(TransactionCodesEnum.SALDO_INSUFICIENTE, result);
        verify(fallback, times(1)).processTransaction(transaction);
    }

    @Test
    void getFallback() {
        assertEquals(CashTransactionStrategy.class, strategy.getFallback().get().getClass());
    }
}