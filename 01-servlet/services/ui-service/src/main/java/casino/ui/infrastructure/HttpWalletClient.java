package casino.ui.infrastructure;

import casino.ui.domain.Account;
import casino.ui.domain.AccountNotFoundException;
import casino.ui.domain.InsufficientFundsException;
import casino.ui.domain.ServiceUnavailableException;
import casino.ui.domain.WalletClient;
import com.google.gson.JsonObject;

import java.util.Map;

public class HttpWalletClient implements WalletClient {

    private static final String ACCOUNTS = "/api/accounts";

    private final HttpJsonClient http;

    public HttpWalletClient(String baseUrl) {
        this.http = new HttpJsonClient("Wallet service", baseUrl);
    }

    @Override
    public Account createOrGet(String username) {
        return toAccount(http.post(ACCOUNTS, Map.of("username", username)));
    }

    @Override
    public Account get(String username) {
        return toAccount(http.get(ACCOUNTS + "/" + username));
    }

    @Override
    public Account debit(String username, long amount) {
        return toAccount(http.post(ACCOUNTS + "/" + username + "/debit", Map.of("amount", String.valueOf(amount))));
    }

    @Override
    public Account credit(String username, long amount) {
        return toAccount(http.post(ACCOUNTS + "/" + username + "/credit", Map.of("amount", String.valueOf(amount))));
    }

    private static Account toAccount(JsonResponse response) {
        return switch (response.status()) {
            case 200 -> {
                JsonObject json = response.json();
                yield new Account(json.get("username").getAsString(), json.get("balance").getAsLong());
            }
            case 400 -> throw new IllegalArgumentException(response.errorMessage());
            case 404 -> throw new AccountNotFoundException(response.errorMessage());
            case 409 -> throw new InsufficientFundsException(response.errorMessage());
            default -> throw new ServiceUnavailableException(
                    "Wallet service returned an unexpected response (HTTP " + response.status() + ")");
        };
    }
}
