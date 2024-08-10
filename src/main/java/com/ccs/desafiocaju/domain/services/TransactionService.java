package com.ccs.desafiocaju.domain.services;

import com.ccs.desafiocaju.api.v1.inputs.TransactionInput;
import jakarta.transaction.Transactional;

public interface TransactionService {
    @Transactional
    String executeTransaction(TransactionInput input);
}
