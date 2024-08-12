package com.ccs.desafiocaju.api.v1.controllers;

import com.ccs.desafiocaju.PostgresConfiguration;
import com.ccs.desafiocaju.api.v1.inputs.TransactionInput;
import com.ccs.desafiocaju.api.v1.outputs.TransactionResponse;
import com.ccs.desafiocaju.domain.models.entities.Account;
import com.ccs.desafiocaju.domain.services.AccountService;
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
import java.util.concurrent.CompletableFuture;

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
    private Account account;
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
            account.setBalanceCash(BigDecimal.valueOf(100000.0));
            account = accountService.save(account);
        }
    }

    @Test
    void testProcess() {
        var input = new TransactionInput(account.getId(), BigDecimal.valueOf(10),
                "5811", "TransactionControllerIntegrationTest");

        var response = restTemplate.postForEntity(url, input, TransactionResponse.class, String.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("00",response.getBody().code());

    }

    @Test
    void testCargaProcess() {
        var qtdRequisicoes = 5000;

        var input = new TransactionInput(account.getId(), BigDecimal.valueOf(10),
                "5811", "TransactionControllerIntegrationTest");

        var responses = new ArrayList<ResponseEntity<TransactionResponse>>();
        var futures = new ArrayList<CompletableFuture<?>>();

        var start = System.currentTimeMillis();

        for (int i = 0; i < qtdRequisicoes; i++) {
            futures.add(CompletableFuture.runAsync(() -> responses
                    .add(restTemplate
                            .postForEntity(url, input, TransactionResponse.class, String.class)))
            );
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        var tempoTotal = (System.currentTimeMillis() - start) / 1000;

        System.out.println("====================================================");
        System.out.println("               Teste de Carga                 ");
        System.out.println("Quantidade de requisições: " + qtdRequisicoes);
        System.out.println("Tempo total: " + tempoTotal + " Segundos");
        System.out.println("Quantidade de requisições por segundo: " + qtdRequisicoes / tempoTotal);
        System.out.println("Tempo médio por request(ms): " + (tempoTotal * 1000 / qtdRequisicoes));
        System.out.println("====================================================");

        responses.forEach(response -> {
            assertTrue(response.getStatusCode().is2xxSuccessful());
            assertEquals("00", response.getBody().code());
        });
    }
}