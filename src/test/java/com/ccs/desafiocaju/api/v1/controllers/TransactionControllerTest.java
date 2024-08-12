package com.ccs.desafiocaju.api.v1.controllers;

import com.ccs.desafiocaju.api.v1.inputs.TransactionInput;
import com.ccs.desafiocaju.domain.services.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;
    @Mock
    private TransactionService transactionService;

    @Test
    void process() {
        var input = new TransactionInput(1L, BigDecimal.valueOf(100.0), "5812", "Padaria John Doe da Silva");

        assertDoesNotThrow(() -> transactionController.process(input));

        verify(transactionService, times(1)).executeTransaction(input);
    }
}