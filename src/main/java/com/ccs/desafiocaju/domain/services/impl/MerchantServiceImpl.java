package com.ccs.desafiocaju.domain.services.impl;

import com.ccs.desafiocaju.domain.repositories.MerchantRepository;
import com.ccs.desafiocaju.domain.services.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;
}
