package com.ccs.desafiocaju.api.v1.controllers;

import com.ccs.desafiocaju.api.v1.inputs.TransactionInput;
import com.ccs.desafiocaju.api.v1.outputs.TransactionResponse;
import com.ccs.desafiocaju.domain.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public TransactionResponse process(@Valid @RequestBody TransactionInput input) {
        return new TransactionResponse(transactionService.executeTransaction(input));
    }
}
