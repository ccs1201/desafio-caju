package com.ccs.desafiocaju.domain.components.impl;

import com.ccs.desafiocaju.domain.components.TransactionStrategy;
import com.ccs.desafiocaju.domain.components.TransactionStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class TransactionStrategyFactoryTest {

    private TransactionStrategyFactory transactionStrategyFactory;

    @Mock
    private TransactionStrategy foodStrategy;

    @Mock
    private TransactionStrategy mealStrategy;

    @Mock
    private TransactionStrategy cashStrategy;

    @BeforeEach
    void setUp() {
        when(foodStrategy.getMccs()).thenReturn(Set.of("5411", "5412"));
        when(mealStrategy.getMccs()).thenReturn(Set.of("5811", "5812"));
        when(cashStrategy.getMccs()).thenReturn(Set.of("cash"));

        List<TransactionStrategy> strategies = Arrays.asList(foodStrategy, mealStrategy, cashStrategy);
        transactionStrategyFactory = new TransactionStrategyFactory(strategies);
    }

    @Test
    void testGetStrategyForFoodMcc() {
        TransactionStrategy strategy = transactionStrategyFactory.getStrategy("5411");
        assertEquals(foodStrategy, strategy);
    }

    @Test
    void testGetStrategyForMealMcc() {
        TransactionStrategy strategy = transactionStrategyFactory.getStrategy("5811");
        assertEquals(mealStrategy, strategy);
    }

    @Test
    void testGetStrategyForCashMcc() {
        TransactionStrategy strategy = transactionStrategyFactory.getStrategy("cash");
        assertEquals(cashStrategy, strategy);
    }

    @Test
    void testGetStrategyForUnknownMcc() {
        TransactionStrategy strategy = transactionStrategyFactory.getStrategy("unknownMcc");
        assertEquals(cashStrategy, strategy);
    }
}