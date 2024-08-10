package com.ccs.desafiocaju.domain.infra.exceptions;

public class CajuException extends RuntimeException {
    public CajuException(String msg) {
        super(msg);
    }
}
