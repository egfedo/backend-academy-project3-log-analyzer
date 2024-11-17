package backend.academy.egfedo.file;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;

public class HTTPLoader implements AutoCloseable {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public void close() {
        httpClient.close();
    }

    public Stream<String> getLines(URI url) throws IOException, InterruptedException {
        return httpClient.send(
            HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build(),
            HttpResponse.BodyHandlers.ofLines()
        ).body();
    }
}
