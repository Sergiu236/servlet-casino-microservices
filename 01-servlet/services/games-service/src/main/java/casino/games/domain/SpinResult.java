package casino.games.domain;

import java.util.List;

public record SpinResult(List<Symbol> reels, long bet, long payout) {

    public boolean isWin() {
        return payout > 0;
    }
}
