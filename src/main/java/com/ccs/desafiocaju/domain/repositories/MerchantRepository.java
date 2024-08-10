package com.ccs.desafiocaju.domain.repositories;

import com.ccs.desafiocaju.domain.models.entities.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    @Query
    Optional<Merchant> findByName(String name);
}