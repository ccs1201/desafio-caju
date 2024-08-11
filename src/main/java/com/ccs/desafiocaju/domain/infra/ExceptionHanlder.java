package com.ccs.desafiocaju.domain.infra;

import com.ccs.desafiocaju.domain.infra.exceptions.CajuException;
import com.ccs.desafiocaju.domain.infra.model.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHanlder {

    @ExceptionHandler(CajuException.class)
    public ExceptionResponse handleCajuException(CajuException ex) {
        log.error("Erro ao processar transação", ex);
        return new ExceptionResponse("07");
    }

    @ExceptionHandler(Exception.class)
    public ExceptionResponse handleException(Exception ex) {
        log.error("Erro ao processar transação", ex);
        return new ExceptionResponse("07");
    }
}
