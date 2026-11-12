package casino.games.infrastructure;

import casino.games.domain.Symbol;
import casino.games.domain.SymbolSource;

import java.util.concurrent.ThreadLocalRandom;

public class RandomSymbolSource implements SymbolSource {

    private static final Symbol[] SYMBOLS = Symbol.values();

    @Override
    public Symbol next() {
        return SYMBOLS[ThreadLocalRandom.current().nextInt(SYMBOLS.length)];
    }
}
