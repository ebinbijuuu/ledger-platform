package com.ledger.platform.service;

import com.ledger.platform.dto.AccountResponse;
import com.ledger.platform.exception.ResourceNotFoundException;
import com.ledger.platform.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<AccountResponse> getAccountsFor(String ownerEmail) {
        return accountRepository.findAllByOwner_EmailOrderByCreatedAtDesc(ownerEmail)
                .stream()
                .map(AccountResponse::from)
                .toList();
    }

    public AccountResponse getAccountFor(UUID accountId, String ownerEmail) {
        return accountRepository.findByIdAndOwner_Email(accountId, ownerEmail)
                .map(AccountResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }
}
