package casino.wallet.domain;

/** The account does not have enough chips for the requested debit. */
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(long balance, long amount) {
        super("Insufficient funds: balance is " + balance + ", tried to debit " + amount);
    }
}
