package com.ledger.platform;

import com.ledger.platform.entity.Account;
import com.ledger.platform.entity.AccountStatus;
import com.ledger.platform.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountDomainTest {

    @Test
    void createsAccountWithUuidAndSupportedValues() {
        Account account = Account.open(
                user("alice@example.com"),
                "ALICE000000000001",
                "GBP",
                AccountStatus.ACTIVE
        );

        assertThat(account.getId()).isNotNull();
        assertThat(account.getOwner().getEmail()).isEqualTo("alice@example.com");
        assertThat(account.getCurrency()).isEqualTo("GBP");
        assertThat(account.getStatus()).isEqualTo(AccountStatus.ACTIVE);
    }

    @Test
    void rejectsBlankOwner() {
        assertThatThrownBy(() -> Account.open(null, "ACCOUNT0001", "GBP", AccountStatus.ACTIVE))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Account owner is required");
    }

    @Test
    void rejectsMalformedAccountNumber() {
        assertThatThrownBy(() -> Account.open(user("alice@example.com"), "bad-1234", "GBP", AccountStatus.ACTIVE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Account number must contain 8 to 34 uppercase letters or digits");
    }

    @Test
    void rejectsCurrencyThatIsNotAnIso4217Code() {
        assertThatThrownBy(() -> Account.open(user("alice@example.com"), "ACCOUNT0001", "ZZZ", AccountStatus.ACTIVE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Currency must be an uppercase ISO 4217 code");
    }

    @Test
    void rejectsMissingStatus() {
        assertThatThrownBy(() -> Account.open(user("alice@example.com"), "ACCOUNT0001", "GBP", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Account status is required");
    }

    private User user(String email) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash("test-hash");
        return user;
    }
}
