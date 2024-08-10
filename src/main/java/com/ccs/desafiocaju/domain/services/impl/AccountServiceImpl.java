package com.ccs.desafiocaju.domain.services.impl;

import com.ccs.desafiocaju.domain.infra.exceptions.CajuException;
import com.ccs.desafiocaju.domain.models.entities.Account;
import com.ccs.desafiocaju.domain.repositories.AccountRepository;
import com.ccs.desafiocaju.domain.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account findByIdLocking(Long id) {
        return accountRepository.findByIdLocking(id).orElseThrow(() ->
                new CajuException("Account not found"));
    }
}
