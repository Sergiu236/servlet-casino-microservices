package casino.ui.infrastructure;

import casino.ui.domain.ServiceUnavailableException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpJsonClient {

    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    private final String serviceName;
    private final String baseUrl;

    public HttpJsonClient(String serviceName, String baseUrl) {
        this.serviceName = serviceName;
        this.baseUrl = baseUrl;
    }

    public JsonResponse get(String path) {
        return send(HttpRequest.newBuilder(URI.create(baseUrl + path)).GET());
    }

    public JsonResponse post(String path, Map<String, String> form) {
        String body = form.entrySet().stream()
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
        return send(HttpRequest.newBuilder(URI.create(baseUrl + path))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body)));
    }

    private JsonResponse send(HttpRequest.Builder request) {
        try {
            HttpResponse<String> response = client.send(
                    request.timeout(Duration.ofSeconds(5)).build(),
                    HttpResponse.BodyHandlers.ofString());
            return new JsonResponse(response.statusCode(), response.body());
        } catch (IOException e) {
            throw new ServiceUnavailableException(serviceName + " is not reachable");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServiceUnavailableException(serviceName + " call was interrupted");
        }
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
