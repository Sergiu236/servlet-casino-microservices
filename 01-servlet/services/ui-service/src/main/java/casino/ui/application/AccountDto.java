package casino.ui.application;

public class AccountDto {

    private final String username;
    private final long balance;

    public AccountDto(String username, long balance) {
        this.username = username;
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public long getBalance() {
        return balance;
    }
}
