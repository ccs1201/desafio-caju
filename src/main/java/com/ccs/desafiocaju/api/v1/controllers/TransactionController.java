package com.ccs.desafiocaju.api.v1.controllers;

import com.ccs.desafiocaju.api.v1.inputs.TransactionInput;
import com.ccs.desafiocaju.api.v1.outputs.TransactionErrorResponse;
import com.ccs.desafiocaju.domain.services.impl.TransactionServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionServiceImpl transactionServiceImpl;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public TransactionErrorResponse create(@Valid @RequestBody TransactionInput input) {
        return new TransactionErrorResponse(transactionServiceImpl.authorizeTransaction(input));
    }
}
