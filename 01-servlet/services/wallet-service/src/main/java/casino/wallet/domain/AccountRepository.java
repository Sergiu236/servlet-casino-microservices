package casino.wallet.domain;

import java.util.Optional;

/**
 * Says WHAT we need from storage, not HOW it is done.
 * The implementation lives in the outer "infrastructure" layer, so the storage can
 * change (e.g. to a database in homework 02) without touching the business rules.
 */
public interface AccountRepository {

    Optional<Account> findByUsername(String username);

    /** Inserts the account, or replaces it if the username already exists. */
    Account save(Account account);
}
