package com.ccs.desafiocaju.domain.services.impl;

import com.ccs.desafiocaju.PostgresConfiguration;
import com.ccs.desafiocaju.api.v1.inputs.TransactionInput;
import com.ccs.desafiocaju.domain.models.entities.Account;
import com.ccs.desafiocaju.domain.models.entities.Merchant;
import com.ccs.desafiocaju.domain.models.enums.TransactionCodesEnum;
import com.ccs.desafiocaju.domain.repositories.MerchantRepository;
import com.ccs.desafiocaju.domain.services.AccountService;
import com.ccs.desafiocaju.domain.services.MerchantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Import(PostgresConfiguration.class)
@SpringBootTest
@Profile("test")
class TransactionServiceImplTest {

    @Autowired
    private TransactionServiceImpl transactionService;

    private static final String MCC_FOOD = "5411";
    private static final String MCC_MEAL = "5811";
    private static final String MCC_CASH = "4211";

    @Autowired
    private AccountService accountService;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    private MerchantService merchantService;

    private Account account;

    @BeforeEach
    void setUp() {

        if (account == null) {
            account = new Account();
            account.setBalanceFood(BigDecimal.valueOf(250.0));
            account.setBalanceMeal(BigDecimal.valueOf(500.0));
            account.setBalanceCash(BigDecimal.valueOf(750.0));
            account = accountService.save(account);
        }
    }

    private TransactionInput getTransactionInput(BigDecimal totalAmount, Merchant merchant) {
        return new TransactionInput(account.getId(), totalAmount, "5411", merchant.getName());
    }

    private Merchant getMerchantTest(String merchantName, String mcc) {
        return merchantService.createMerchant(merchantName, mcc);
    }


    @Test
    void testExecuteTransactionFoodSucesso() {
        var merchant = getMerchantTest("testExecuteTransactionFoodSucesso", MCC_FOOD);
        merchantRepository.save(merchant);

        String result = transactionService.executeTransaction(getTransactionInput(BigDecimal.valueOf(100.0), merchant));

        assertEquals(TransactionCodesEnum.APROVADA.getValue(), result);

        var accountUpdated = accountService.findById(account.getId());

        assertEquals(BigDecimal.valueOf(150.00).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceFood().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(500.00).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceMeal().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(750.00).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceCash().setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void testExecuteTransactionQuadoFallback() {
        var merchant = getMerchantTest("testExecuteTransactionQuadoFallback", MCC_FOOD);
        merchantRepository.save(merchant);

        String result = transactionService.executeTransaction(getTransactionInput(BigDecimal.valueOf(735.0), merchant));
        var accountUpdated = accountService.findById(account.getId());

        assertEquals(TransactionCodesEnum.APROVADA.getValue(), result);
        assertEquals(BigDecimal.valueOf(15.00).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceCash().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(500.00).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceMeal().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(250.00).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceFood().setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void testExecuteTransactionQuandoFallbackSaldoInsuficiente() {
        var merchant = getMerchantTest("testExecuteTransactionQuandoFallbackSaldoInsuficiente", MCC_FOOD);
        merchantRepository.save(merchant);

        String result = transactionService.executeTransaction(getTransactionInput(BigDecimal.valueOf(750.01), merchant));

        assertEquals(TransactionCodesEnum.SALDO_INSUFICIENTE.getValue(), result);

        var accountUpdated = accountService.findById(account.getId());

        assertEquals(BigDecimal.valueOf(750).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceCash().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceMeal().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(250).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceFood().setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void testExecuteTransactionQuandoFallbackNaoDisponivel() {
        var merchant = getMerchantTest("testExecuteTransactionQuandoFallbackNaoDisponivel", MCC_CASH);
        merchantRepository.save(merchant);

        String result = transactionService.executeTransaction(getTransactionInput(BigDecimal.valueOf(750.01), merchant));

        assertEquals(TransactionCodesEnum.SALDO_INSUFICIENTE.getValue(), result);

        var accountUpdated = accountService.findById(account.getId());

        assertEquals(BigDecimal.valueOf(750).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceCash().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceMeal().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(250).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceFood().setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void testExecuteTransactionMealSucesso() {
        var merchant = getMerchantTest("testExecuteTransactionMealSucesso", MCC_MEAL);
        merchantRepository.save(merchant);

        String result = transactionService.executeTransaction(getTransactionInput(BigDecimal.valueOf(100.0), merchant));

        assertEquals(TransactionCodesEnum.APROVADA.getValue(), result);

        var accountUpdated = accountService.findById(account.getId());

        assertEquals(BigDecimal.valueOf(750).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceCash().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(400).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceMeal().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(250).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceFood().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void testExecuteTransactionMealFallback() {
        var merchant = getMerchantTest("testExecuteTransactionMealFallback", MCC_MEAL);
        merchantRepository.save(merchant);

        String result = transactionService.executeTransaction(getTransactionInput(BigDecimal.valueOf(600.0), merchant));

        assertEquals(TransactionCodesEnum.APROVADA.getValue(), result);

        var accountUpdated = accountService.findById(account.getId());

        assertEquals(BigDecimal.valueOf(150).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceCash().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceMeal().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(250).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceFood().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void testConcorrenteAteZerarSaldos() {

        var merchantMeal = getMerchantTest("Zerar Meal", MCC_MEAL);
        var merchantFood = getMerchantTest("Zerar Food", MCC_FOOD);
        var merchantCash = getMerchantTest("Zerar Cash", MCC_CASH);
        merchantRepository.save(merchantMeal);
        merchantRepository.save(merchantFood);
        merchantRepository.save(merchantCash);

        var futures = new ArrayList<CompletableFuture<?>>();

        for (int i = 0; i < 100; i++) {
            futures.add(CompletableFuture.runAsync(() ->
                    transactionService.executeTransaction(getTransactionInput(BigDecimal.valueOf(2.5), merchantFood))));
        }

        for (int i = 0; i < 100; i++) {
            futures.add(CompletableFuture.runAsync(() ->
                    transactionService.executeTransaction(getTransactionInput(BigDecimal.valueOf(5.0), merchantMeal))));
        }

        for (int i = 0; i < 100; i++) {
            futures.add(CompletableFuture.runAsync(() ->
                    transactionService.executeTransaction(getTransactionInput(BigDecimal.valueOf(7.5), merchantCash))));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        var accountUpdated = accountService.findById(account.getId());
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceCash().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceMeal().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceFood().setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void testConcorrenteDuasTransacoesPorSaldo() {

        var merchantMeal = getMerchantTest("Duas por Meal", MCC_MEAL);
        var merchantFood = getMerchantTest("Duas por Food", MCC_FOOD);
        var merchantCash = getMerchantTest("Duas por Cash", MCC_CASH);
        merchantRepository.save(merchantMeal);
        merchantRepository.save(merchantFood);
        merchantRepository.save(merchantCash);

        var futures = new ArrayList<CompletableFuture<?>>();

        for (int i = 0; i < 2; i++) {
            futures.add(CompletableFuture.runAsync(() ->
                    transactionService.executeTransaction(getTransactionInput(BigDecimal.valueOf(75), merchantFood))));
        }

        for (int i = 0; i < 2; i++) {
            futures.add(CompletableFuture.runAsync(() ->
                    transactionService.executeTransaction(getTransactionInput(BigDecimal.valueOf(125), merchantMeal))));
        }

        for (int i = 0; i < 2; i++) {
            futures.add(CompletableFuture.runAsync(() ->
                    transactionService.executeTransaction(getTransactionInput(BigDecimal.valueOf(250), merchantCash))));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        var accountUpdated = accountService.findById(account.getId());
        assertEquals(BigDecimal.valueOf(250L).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceCash().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(250L).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceMeal().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceFood().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void testConcorrenteAteZerarSaldoMeal() {

        var merchantMeal = getMerchantTest("testConcorrenteAteSaldoFoodECash", MCC_MEAL);
        merchantRepository.save(merchantMeal);

        var futures = new ArrayList<CompletableFuture<?>>();

        for (int i = 0; i < 100; i++) {
            futures.add(CompletableFuture.runAsync(() ->
                    transactionService.executeTransaction(getTransactionInput(BigDecimal.valueOf(5), merchantMeal))));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        var result = transactionService.executeTransaction(getTransactionInput(BigDecimal.valueOf(750.01), merchantMeal));

        assertEquals(TransactionCodesEnum.SALDO_INSUFICIENTE.getValue(), result);

        var accountUpdated = accountService.findById(account.getId());

        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceMeal().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(750).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceCash().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(250).setScale(2, RoundingMode.HALF_UP), accountUpdated.getBalanceFood().setScale(2, RoundingMode.HALF_UP));
    }

}