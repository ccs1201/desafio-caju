package com.ccs.desafiocaju.domain.repositories;

import com.ccs.desafiocaju.domain.models.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}