package casino.ui.application;

import casino.ui.domain.Account;
import casino.ui.domain.GamesClient;
import casino.ui.domain.SpinResult;
import casino.ui.domain.WalletClient;

import java.util.regex.Pattern;

public class CasinoService {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("[A-Za-z0-9_]{1,30}");

    private final WalletClient walletClient;
    private final GamesClient gamesClient;

    public CasinoService(WalletClient walletClient, GamesClient gamesClient) {
        this.walletClient = walletClient;
        this.gamesClient = gamesClient;
    }

    public AccountDto login(String username) {
        String name = username == null ? "" : username.trim();
        if (!USERNAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("Username must be 1-30 letters, digits or underscores");
        }
        return toDto(walletClient.createOrGet(name));
    }

    public AccountDto getAccount(String username) {
        return toDto(walletClient.get(username));
    }

    public PlayResultDto play(String username, long bet) {
        Account afterBet = walletClient.debit(username, bet);

        SpinResult spin;
        try {
            spin = gamesClient.spin(bet);
        } catch (RuntimeException e) {
            walletClient.credit(username, bet);
            throw e;
        }

        Account finalAccount = spin.isWin() ? walletClient.credit(username, spin.payout()) : afterBet;
        return new PlayResultDto(spin.reels(), spin.bet(), spin.payout(), spin.isWin(), finalAccount.balance());
    }

    private static AccountDto toDto(Account account) {
        return new AccountDto(account.username(), account.balance());
    }
}
