package com.ccs.desafiocaju.domain.services;

import com.ccs.desafiocaju.domain.models.entities.Merchant;

import java.util.Optional;

public interface MerchantService {
    Optional<Merchant> findByName(String name);

    Merchant createMerchant(String name, String mcc);
}
