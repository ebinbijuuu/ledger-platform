package com.ledger.platform;

import com.ledger.platform.entity.Account;
import com.ledger.platform.entity.AccountStatus;
import com.ledger.platform.entity.User;
import com.ledger.platform.repository.AccountRepository;
import com.ledger.platform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AccountApiIntegrationTest {

    private static final String ALICE = "alice@example.com";
    private static final String BOB = "bob@example.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    private Account aliceAccount;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
        User alice = userRepository.save(createUser(ALICE));
        User bob = userRepository.save(createUser(BOB));
        aliceAccount = accountRepository.saveAndFlush(
                Account.open(alice, "ALICE000000000001", "GBP", AccountStatus.ACTIVE)
        );
        accountRepository.saveAndFlush(
                Account.open(alice, "ALICE000000000002", "EUR", AccountStatus.FROZEN)
        );
        accountRepository.saveAndFlush(
                Account.open(bob, "BOB00000000000001", "USD", AccountStatus.CLOSED)
        );
    }

    @Test
    void listReturnsOnlyTheAuthenticatedUsersAccounts() throws Exception {
        mockMvc.perform(get("/api/v1/accounts").with(user(ALICE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id").isArray())
                .andExpect(jsonPath("$[*].accountNumber", containsInAnyOrder(
                        "ALICE000000000001",
                        "ALICE000000000002"
                )))
                .andExpect(jsonPath("$[2]").doesNotExist())
                .andExpect(jsonPath("$[0].owner").doesNotExist());
    }

    @Test
    void listForAnotherUserDoesNotExposeTheFirstUsersAccounts() throws Exception {
        mockMvc.perform(get("/api/v1/accounts").with(user(BOB)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").value("BOB00000000000001"))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void getReturnsAccountDetailsForItsOwner() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/{accountId}", aliceAccount.getId())
                        .with(user(ALICE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(aliceAccount.getId().toString()))
                .andExpect(jsonPath("$.accountNumber").value("ALICE000000000001"))
                .andExpect(jsonPath("$.currency").value("GBP"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void anotherUsersAccountIsReportedAsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/{accountId}", aliceAccount.getId())
                        .with(user(BOB)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Account not found"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/accounts/" + aliceAccount.getId()));
    }

    @Test
    void missingAccountUsesTheSharedNotFoundError() throws Exception {
        mockMvc.perform(get(
                                "/api/v1/accounts/{accountId}",
                                "00000000-0000-0000-0000-000000000001"
                        ).with(user(ALICE)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Account not found"));
    }

    @Test
    void malformedAccountIdUsesTheSharedValidationError() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/not-a-uuid").with(user(ALICE)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.fieldErrors.accountId").value("Invalid value"));
    }

    @Test
    void accountEndpointsRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/accounts"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void accountNumberMustBeUniqueAcrossOwners() {
        assertThatThrownBy(() -> accountRepository.saveAndFlush(
                Account.open(userRepository.findByEmail(BOB).orElseThrow(),
                        "ALICE000000000001", "GBP", AccountStatus.ACTIVE)
        )).isInstanceOf(DataIntegrityViolationException.class);
    }

    private User createUser(String email) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash("test-hash");
        return user;
    }
}
