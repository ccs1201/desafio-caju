package com.ccs.desafiocaju.domain.models.enums;

import lombok.Getter;

@Getter
public enum TransactionCodesEnum {

    APROVADA("00"),
    REJEITADA("51"),
    ERRO("07");

    private final String value;

    TransactionCodesEnum(String value) {
        this.value = value;
    }

}
