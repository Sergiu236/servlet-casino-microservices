package casino.ui.application;

import java.util.List;

public class PlayResultDto {

    private final List<String> reels;
    private final long bet;
    private final long payout;
    private final boolean win;
    private final long balance;

    public PlayResultDto(List<String> reels, long bet, long payout, boolean win, long balance) {
        this.reels = reels;
        this.bet = bet;
        this.payout = payout;
        this.win = win;
        this.balance = balance;
    }

    public List<String> getReels() {
        return reels;
    }

    public long getBet() {
        return bet;
    }

    public long getPayout() {
        return payout;
    }

    public boolean isWin() {
        return win;
    }

    public long getBalance() {
        return balance;
    }
}
