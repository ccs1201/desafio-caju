package com.ccs.desafiocaju.domain.services.impl;

import com.ccs.desafiocaju.domain.models.entities.Merchant;
import com.ccs.desafiocaju.domain.repositories.MerchantRepository;
import com.ccs.desafiocaju.domain.services.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;

    @Override
    public Optional<Merchant> findByName(String name) {
        return merchantRepository
                .findByName(name);
    }

    @Override
    public Merchant createMerchant(String name, String mcc) {
        return merchantRepository.save(Merchant.builder()
                .name(name)
                .mcc(mcc)
                .transactions(Collections.emptyList())
                .build());
    }
}
