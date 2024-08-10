package com.ccs.desafiocaju.domain.services.impl;

import com.ccs.desafiocaju.api.v1.inputs.TransactionInput;
import com.ccs.desafiocaju.domain.components.TransactionStrategyFactory;
import com.ccs.desafiocaju.domain.components.impl.CashTransactionStrategy;
import com.ccs.desafiocaju.domain.infra.exceptions.CajuInsufficientBalanceException;
import com.ccs.desafiocaju.domain.models.entities.Merchant;
import com.ccs.desafiocaju.domain.models.entities.Transaction;
import com.ccs.desafiocaju.domain.models.enums.TransactionCodesEnum;
import com.ccs.desafiocaju.domain.repositories.TransactionRepository;
import com.ccs.desafiocaju.domain.services.AccountService;
import com.ccs.desafiocaju.domain.services.MerchantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final TransactionStrategyFactory transactionStrategyFactory;
    private final CashTransactionStrategy cashTransactionStrategy;
    private final MerchantService merchantService;

    @Transactional
    public String authorizeTransaction(TransactionInput input) {
        var account = accountService.findByIdLocking(input.account());
        var merchant = getOrCreateMerchant(input);
        var transaction = Transaction.builder()
                .account(account)
                .amount(input.totalAmount())
                .merchant(merchant)
                .mcc(input.mcc())
                .build();
        try {
            var transactionCode = process(transaction);

            transactionRepository.save(transaction);

            return transactionCode.getValue();

        } catch (CajuInsufficientBalanceException e) {
            log.debug("Saldo insuficiente");
            return fallback(transaction).getValue();
        }
    }

    private TransactionCodesEnum fallback(Transaction transaction) {
        log.debug("Fallback to saldo CASH");
        try {
            var transactionCode = cashTransactionStrategy.processTransaction(transaction);
            log.debug("Saldo CASH utilizado com sucesso");
            transactionRepository.save(transaction);
            return transactionCode;
        } catch (CajuInsufficientBalanceException e) {
            log.debug("Saldo CASH insuficiente");
            return TransactionCodesEnum.SALDO_INSUFICIENTE;
        }
    }

    private TransactionCodesEnum process(Transaction transaction) {
        return transactionStrategyFactory
                .getStrategy(transaction.getMcc())
                .processTransaction(transaction);
    }

    private Merchant getOrCreateMerchant(TransactionInput transaction) {
        return merchantService.findByName(transaction.merchant())
                .orElseGet(() -> merchantService
                        .createMerchant(transaction.merchant(), transaction.mcc()));
    }
}
