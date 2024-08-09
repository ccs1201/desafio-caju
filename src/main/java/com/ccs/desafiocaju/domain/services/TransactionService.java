package com.ccs.desafiocaju.domain.services;

import com.ccs.desafiocaju.api.v1.inputs.TransactionInput;
import com.ccs.desafiocaju.domain.models.entities.Transaction;
import com.ccs.desafiocaju.domain.models.enums.TransactionCodesEnum;
import com.ccs.desafiocaju.domain.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public String authorizeTransaction(TransactionInput input) {
        try {
            var transaction = Transaction.builder().build();
            return TransactionCodesEnum.REJEITADA.getValue();
        } catch (Exception e) {
            log.error("Error authorizing transaction", e);
            return TransactionCodesEnum.ERRO.getValue();
        }
    }
}
