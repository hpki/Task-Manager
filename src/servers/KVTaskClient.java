package servers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private String API_KEY;
    private HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
    private HttpClient client = HttpClient.newHttpClient();
    private URI url;

    public KVTaskClient(URI url) {
        this.url = url;
        API_KEY = registerApiKey();
    }

    public URI getUrl() {
        return url;
    }

    public void put(String key, String json) throws IOException, InterruptedException {

        HttpRequest request = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(url + "/save/" + key + "?API_KEY=" + API_KEY)).version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public String load(String key) {
        HttpRequest request = requestBuilder.GET().uri(URI.create(url + "/load/" + key + "?API_KEY=" + API_KEY)).version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new KVTaskClientException("Ошибка при загрузке");
        }
        return response.body();
    }


    String registerApiKey() {
        HttpRequest request = requestBuilder.GET().uri(URI.create(url + "/register")).version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new KVTaskClientException("Ошибка при регистрации");
        }
        return response.body();
    }

    public class KVTaskClientException extends RuntimeException {
        public KVTaskClientException(final String message) {
            super(message);
        }
    }

}
