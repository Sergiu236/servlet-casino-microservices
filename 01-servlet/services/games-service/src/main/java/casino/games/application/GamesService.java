package casino.games.application;

import casino.games.domain.SpinResult;
import casino.games.domain.Symbol;
import casino.games.domain.SymbolSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GamesService {

    public static final long MIN_BET = 1;
    public static final long MAX_BET = 500;
    private static final int REEL_COUNT = 3;

    private final SymbolSource symbolSource;

    public GamesService(SymbolSource symbolSource) {
        this.symbolSource = symbolSource;
    }

    public SpinResultDto spin(long bet) {
        validateBet(bet);
        List<Symbol> reels = drawReels();
        SpinResult result = new SpinResult(reels, bet, calculatePayout(reels, bet));
        return toDto(result);
    }

    private List<Symbol> drawReels() {
        List<Symbol> reels = new ArrayList<>();
        for (int i = 0; i < REEL_COUNT; i++) {
            reels.add(symbolSource.next());
        }
        return reels;
    }

    private static long calculatePayout(List<Symbol> reels, long bet) {
        int distinctSymbols = new HashSet<>(reels).size();
        return switch (distinctSymbols) {
            case 1 -> bet * reels.get(0).multiplier();
            case 2 -> bet;
            default -> 0;
        };
    }

    private static SpinResultDto toDto(SpinResult result) {
        List<String> reelNames = result.reels().stream().map(Symbol::name).toList();
        return new SpinResultDto(reelNames, result.bet(), result.payout(), result.isWin());
    }

    private static void validateBet(long bet) {
        if (bet < MIN_BET || bet > MAX_BET) {
            throw new IllegalArgumentException("Bet must be between " + MIN_BET + " and " + MAX_BET);
        }
    }
}
