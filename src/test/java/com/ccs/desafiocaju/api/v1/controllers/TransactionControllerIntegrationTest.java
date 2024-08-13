package com.ccs.desafiocaju.api.v1.controllers;

import com.ccs.desafiocaju.PostgresConfiguration;
import com.ccs.desafiocaju.api.v1.inputs.TransactionInput;
import com.ccs.desafiocaju.api.v1.outputs.TransactionResponse;
import com.ccs.desafiocaju.domain.models.entities.Account;
import com.ccs.desafiocaju.domain.models.entities.Merchant;
import com.ccs.desafiocaju.domain.services.AccountService;
import com.ccs.desafiocaju.domain.services.MerchantService;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(PostgresConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MerchantService merchantService;

    private Account account;
    private Merchant merchantSync;
    private static String url;

    @PostConstruct
    public void setup() {
        url = "http://localhost:%d/api/v1/transactions".formatted(port);
    }

    @BeforeEach
    void setUp() {
        if (account == null) {
            account = new Account();
            account.setBalanceFood(BigDecimal.valueOf(250.0));
            account.setBalanceMeal(BigDecimal.valueOf(500.0));
            account.setBalanceCash(BigDecimal.valueOf(100.0));
            account = accountService.save(account);
        }
    }

    @Test
    void testProcess() {

        merchantSync = merchantService.createMerchant("Process TransactionControllerIntegrationTest", "5811");

        var input = new TransactionInput(account.getId(), BigDecimal.valueOf(10),
                "5811", merchantSync.getName());

        var response = restTemplate.postForEntity(url, input, TransactionResponse.class, String.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("00", response.getBody().code());

    }

    @Test
    void loadTests() {
        merchantSync = merchantService.createMerchant("Teste Carga Sync TransactionControllerIntegrationTest", "5811");
        var merchantAsync = merchantService.createMerchant("Teste Carga Async TransactionControllerIntegrationTest", "5811");

        var qtdTestes = 5;

        for (int i = 0; i < qtdTestes; i++) {
            testCargaProcess(merchantSync, i);
            testCargaProcessAsync(merchantAsync, i);
        }
    }


    void testCargaProcess(Merchant merchant, int numeroTest) {
        var qtdRequisicoes = 1000;

        var responses = new ArrayList<ResponseEntity<TransactionResponse>>();
        var futures = new ArrayList<CompletableFuture<?>>(qtdRequisicoes);

        var listInputs = new ArrayList<TransactionInput>(qtdRequisicoes);

        for (int i = 0; i < qtdRequisicoes; i++) {
            account.setId(null);
            account.setTransactions(Collections.emptyList());
            listInputs.add(new TransactionInput(accountService.save(account).getId(), BigDecimal.valueOf(10),
                    "5811", merchant.getName()));
        }

        var start = System.nanoTime();
        for (int i = 0; i < qtdRequisicoes; i++) {
            final var id = i;
            futures.add(CompletableFuture.runAsync(() -> responses
                            .add(restTemplate.postForEntity(url, listInputs.get(id), TransactionResponse.class)),
                    ForkJoinPool.commonPool())
            );
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[qtdRequisicoes])).join();

        var tempoTotal = (System.nanoTime() - start);
        var tempoMedioPorRequisicao = tempoTotal / (double) qtdRequisicoes;
        var tempoTotalSegundos = tempoTotal / 1_000_000_000.0;  // Convertendo nanosegundos para segundos

        System.out.println("====================================================");
        System.out.printf("               Teste de Carga Sync (%d)%n", numeroTest + 1);
        System.out.println("Quantidade de requisições: " + qtdRequisicoes);
        System.out.println("Tempo total: " + tempoTotal + " Ns");
        System.out.println("Tempo médio por requisição: " + tempoMedioPorRequisicao + " Ns");
        System.out.println("Quantidade de requisições por Ms: " + ((double) qtdRequisicoes / (tempoTotal / 1_000_000.0)));
        System.out.println("Quantidade de requisições por segundo: " + (qtdRequisicoes / tempoTotalSegundos));
        System.out.println("====================================================");


        responses.forEach(response -> {
            assertTrue(response.getStatusCode().is2xxSuccessful());
            assertEquals("00", response.getBody().code());
        });
    }


    void testCargaProcessAsync(Merchant merchant, int numeroTest) {
        var qtdRequisicoes = 1000;

        var responses = new ArrayList<ResponseEntity<TransactionResponse>>();
        var futures = new ArrayList<CompletableFuture<?>>(qtdRequisicoes);

        var listInputs = new ArrayList<TransactionInput>(qtdRequisicoes);

        for (int i = 0; i < qtdRequisicoes; i++) {
            account.setId(null);
            account.setTransactions(Collections.emptyList());
            listInputs.add(new TransactionInput(accountService.save(account).getId(), BigDecimal.valueOf(10),
                    "5811", merchant.getName()));
        }

        var start = System.nanoTime();
        for (int i = 0; i < qtdRequisicoes; i++) {
            final var id = i;
            futures.add(CompletableFuture.runAsync(() -> responses
                            .add(restTemplate.postForEntity(url.concat("/async"), listInputs.get(id), TransactionResponse.class)),
                    ForkJoinPool.commonPool())
            );
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[qtdRequisicoes])).join();

        var tempoTotal = (System.nanoTime() - start);
        var tempoMedioPorRequisicao = tempoTotal / (double) qtdRequisicoes;
        var tempoTotalSegundos = tempoTotal / 1_000_000_000.0;  // Convertendo nanosegundos para segundos

        System.out.println("====================================================");
        System.out.printf("               Teste de Carga Async (%d)%n", numeroTest + 1);
        System.out.println("Quantidade de requisições: " + qtdRequisicoes);
        System.out.println("Tempo total: " + tempoTotal + " Ns");
        System.out.println("Tempo médio por requisição: " + tempoMedioPorRequisicao + " Ns");
        System.out.println("Quantidade de requisições por Ms: " + ((double) qtdRequisicoes / (tempoTotal / 1_000_000.0)));
        System.out.println("Quantidade de requisições por segundo: " + (qtdRequisicoes / tempoTotalSegundos));
        System.out.println("====================================================");

        responses.forEach(response -> {
            assertTrue(response.getStatusCode().is2xxSuccessful());
            assertEquals("00", response.getBody().code());
        });
    }
}