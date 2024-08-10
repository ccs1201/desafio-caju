package com.ccs.desafiocaju.domain.infra.exceptions;

import com.ccs.desafiocaju.domain.models.enums.TransactionCodesEnum;
import lombok.Getter;

@Getter
public class CajuInsufficientBalanceException extends CajuException {
    private final TransactionCodesEnum code = TransactionCodesEnum.SALDO_INSUFICIENTE;

    public CajuInsufficientBalanceException(String tipoSaldo) {
        super("%S Saldo insuficiente".formatted(tipoSaldo));
    }
}
