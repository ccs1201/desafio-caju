package com.ccs.desafiocaju.domain.services;

import com.ccs.desafiocaju.domain.models.entities.Account;

public interface AccountService {
    Account findByIdLocking(Long id);

    Account findById(Long id);

    Account save(Account account);
}
