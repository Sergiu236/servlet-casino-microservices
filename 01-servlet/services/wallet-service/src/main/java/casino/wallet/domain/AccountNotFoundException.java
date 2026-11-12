package casino.wallet.domain;

/** The requested username has no account. */
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String username) {
        super("Account not found: " + username);
    }
}
