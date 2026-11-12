package casino.ui.domain;

import java.util.List;

public record SpinResult(List<String> reels, long bet, long payout) {

    public boolean isWin() {
        return payout > 0;
    }
}
