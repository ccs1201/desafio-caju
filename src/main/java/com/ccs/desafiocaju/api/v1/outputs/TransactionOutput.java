package com.ccs.desafiocaju.api.v1.outputs;

import jakarta.validation.constraints.NotBlank;

public record TransactionOutput(@NotBlank String code) {
}
