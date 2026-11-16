package com.ledger.platform.controller;

import com.ledger.platform.dto.AccountResponse;
import com.ledger.platform.service.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<AccountResponse> getAccounts(Authentication authentication) {
        return accountService.getAccountsFor(authentication.getName());
    }

    @GetMapping("/{accountId}")
    public AccountResponse getAccount(
            @PathVariable UUID accountId,
            Authentication authentication
    ) {
        return accountService.getAccountFor(accountId, authentication.getName());
    }
}
