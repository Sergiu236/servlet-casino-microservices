package casino.wallet.application;

import casino.wallet.domain.Account;
import casino.wallet.domain.AccountNotFoundException;
import casino.wallet.domain.AccountRepository;
import casino.wallet.domain.InsufficientFundsException;

/**
 * The business rules of the wallet. It knows nothing about HTTP or JSON,
 * and talks to storage only through the AccountRepository interface.
 *
 * Methods are synchronized because the server handles requests on many threads:
 * "read balance, check it, write new balance" must not be interleaved by two requests.
 */
public class WalletService {

    public static final long STARTING_BALANCE = 1000;
    private static final int MAX_USERNAME_LENGTH = 30;

    private final AccountRepository repository;

    public WalletService(AccountRepository repository) {
        this.repository = repository;
    }

    /** Login: returns the existing account, or creates one with the starting balance. */
    public synchronized AccountDto createOrGet(String username) {
        String name = validateUsername(username);
        Account account = repository.findByUsername(name)
                .orElseGet(() -> repository.save(new Account(name, STARTING_BALANCE)));
        return toDto(account);
    }

    public synchronized AccountDto get(String username) {
        return toDto(find(username));
    }

    /** Takes chips from the account (the bet). Fails if the balance is too low. */
    public synchronized AccountDto debit(String username, long amount) {
        requirePositive(amount);
        Account account = find(username);
        if (account.balance() < amount) {
            throw new InsufficientFundsException(account.balance(), amount);
        }
        return toDto(repository.save(account.withBalance(account.balance() - amount)));
    }

    /** Adds chips to the account (the payout). */
    public synchronized AccountDto credit(String username, long amount) {
        requirePositive(amount);
        Account account = find(username);
        return toDto(repository.save(account.withBalance(account.balance() + amount)));
    }

    private Account find(String username) {
        String name = validateUsername(username);
        return repository.findByUsername(name)
                .orElseThrow(() -> new AccountNotFoundException(name));
    }

    private static AccountDto toDto(Account account) {
        return new AccountDto(account.username(), account.balance());
    }

    private static String validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        String name = username.trim();
        if (name.length() > MAX_USERNAME_LENGTH) {
            throw new IllegalArgumentException("Username is too long (max " + MAX_USERNAME_LENGTH + ")");
        }
        return name;
    }

    private static void requirePositive(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
    }
}
