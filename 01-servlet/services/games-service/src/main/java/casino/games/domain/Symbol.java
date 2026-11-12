package casino.games.domain;

public enum Symbol {
    CHERRY(5),
    LEMON(8),
    ORANGE(10),
    BELL(15),
    SEVEN(20);

    private final int multiplier;

    Symbol(int multiplier) {
        this.multiplier = multiplier;
    }

    public int multiplier() {
        return multiplier;
    }
}
