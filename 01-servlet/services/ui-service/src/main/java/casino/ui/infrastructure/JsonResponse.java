package casino.ui.infrastructure;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public record JsonResponse(int status, String body) {

    public JsonObject json() {
        try {
            return JsonParser.parseString(body).getAsJsonObject();
        } catch (RuntimeException e) {
            return new JsonObject();
        }
    }

    public String errorMessage() {
        JsonElement error = json().get("error");
        return error == null ? "Unexpected response from service (HTTP " + status + ")" : error.getAsString();
    }
}
