package com.ccs.desafiocaju.api.v1.inputs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionInput(
        @NotNull Long account,
        @NotNull @Positive BigDecimal totalAmount,
        @NotBlank String mcc,
        @NotBlank String merchant) {

}
