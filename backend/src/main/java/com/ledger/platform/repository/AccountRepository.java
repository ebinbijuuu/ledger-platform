package com.ledger.platform.repository;

import com.ledger.platform.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    List<Account> findAllByOwner_EmailOrderByCreatedAtDesc(String ownerEmail);

    Optional<Account> findByIdAndOwner_Email(UUID id, String ownerEmail);
}
