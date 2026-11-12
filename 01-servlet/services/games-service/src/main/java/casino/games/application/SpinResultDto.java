package casino.games.application;

import java.util.List;

public record SpinResultDto(List<String> reels, long bet, long payout, boolean win) {
}
