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
class FoodTransactionStrategyTest {

    @InjectMocks
    private FoodTransactionStrategy strategy;
    @Mock
    private CashTransactionStrategy fallback;
    private Transaction transaction;
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setBalanceFood(new BigDecimal("100.00"));

        transaction = new Transaction();
        transaction.setAccount(account);
    }

    @Test
    void testProcessTransactionAprovada() {
        transaction.setAmount(new BigDecimal("50.00"));

        TransactionCodesEnum result = strategy.processTransaction(transaction);

        assertEquals(TransactionCodesEnum.APROVADA, result);
        assertEquals(new BigDecimal("50.00"), account.getBalanceFood());
        assertEquals(TransactionBalanceTypeEnum.FOOD, transaction.getTransactionBalanceType());
        verify(fallback, never()).processTransaction(transaction);
    }

    @Test
    void testProcessTransactionSaldoInsuficiente() {

        when(fallback.processTransaction(transaction)).thenReturn(TransactionCodesEnum.SALDO_INSUFICIENTE);
        transaction.setAmount(new BigDecimal("150.00"));

        var result = strategy.processTransaction(transaction);

        assertEquals(new BigDecimal("100.00"), account.getBalanceFood());
        assertEquals(TransactionCodesEnum.SALDO_INSUFICIENTE, result);
        verify(fallback, times(1)).processTransaction(transaction);
    }

    @ParameterizedTest
    @ValueSource(strings = {"5411", "5412"})
    void getMccs(String mcc) {
        assertTrue(strategy.getMccs().contains(mcc));
    }

    @Test
    void getFallback() {
        assertEquals(CashTransactionStrategy.class, strategy.getFallback().get().getClass());
    }
}