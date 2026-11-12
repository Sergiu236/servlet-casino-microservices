package casino.ui.domain;

public interface WalletClient {

    Account createOrGet(String username);

    Account get(String username);

    Account debit(String username, long amount);

    Account credit(String username, long amount);
}
