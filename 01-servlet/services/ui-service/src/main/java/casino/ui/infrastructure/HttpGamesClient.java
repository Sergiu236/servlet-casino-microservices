package casino.ui.infrastructure;

import casino.ui.domain.GamesClient;
import casino.ui.domain.ServiceUnavailableException;
import casino.ui.domain.SpinResult;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class HttpGamesClient implements GamesClient {

    private final HttpJsonClient http;

    public HttpGamesClient(String baseUrl) {
        this.http = new HttpJsonClient("Games service", baseUrl);
    }

    @Override
    public SpinResult spin(long bet) {
        JsonResponse response = http.post("/api/slots/spin", Map.of("bet", String.valueOf(bet)));
        return switch (response.status()) {
            case 200 -> toSpinResult(response.json());
            case 400 -> throw new IllegalArgumentException(response.errorMessage());
            default -> throw new ServiceUnavailableException(
                    "Games service returned an unexpected response (HTTP " + response.status() + ")");
        };
    }

    private static SpinResult toSpinResult(JsonObject json) {
        var reels = json.getAsJsonArray("reels").asList().stream().map(JsonElement::getAsString).toList();
        return new SpinResult(reels, json.get("bet").getAsLong(), json.get("payout").getAsLong());
    }
}
