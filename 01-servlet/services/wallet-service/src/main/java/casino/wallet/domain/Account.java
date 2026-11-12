package casino.wallet.domain;

/**
 * A player's wallet: a username and a number of chips.
 * Immutable: changing the balance creates a new Account.
 */
public record Account(String username, long balance) {

    public Account withBalance(long newBalance) {
        return new Account(username, newBalance);
    }
}
