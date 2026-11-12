package casino.wallet.infrastructure;

import casino.wallet.domain.Account;
import casino.wallet.domain.AccountRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Keeps accounts in a map in memory. Everything is lost when the server restarts.
 * ConcurrentHashMap is safe when many requests (threads) use it at the same time.
 */
public class InMemoryAccountRepository implements AccountRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> findByUsername(String username) {
        return Optional.ofNullable(accounts.get(username));
    }

    @Override
    public Account save(Account account) {
        accounts.put(account.username(), account);
        return account;
    }
}
