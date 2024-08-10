package com.ccs.desafiocaju.domain.repositories;

import com.ccs.desafiocaju.domain.models.entities.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
}