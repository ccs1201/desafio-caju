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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

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

        assertEquals(TransactionCodesEnum.APROVADA, result);  // Transação aprovada
        assertEquals(new BigDecimal("50.00"), account.getBalanceFood());
    }

    @Test
    void testProcessTransactionSaldoInsuficiente() {
        transaction.setAmount(new BigDecimal("150.00"));

        assertThrows(CajuInsufficientBalanceException.class, () -> strategy.processTransaction(transaction));

        assertEquals(new BigDecimal("100.00"), account.getBalanceFood());  // Saldo não deve ser alterado
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