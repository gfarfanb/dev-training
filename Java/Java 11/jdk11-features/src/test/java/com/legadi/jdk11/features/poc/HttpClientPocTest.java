package com.legadi.jdk11.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.UUID;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.legadi.jdk11.features.poc.embedded.EmbeddedRestAbstractTest;

@Tag("JEP-321")
public class HttpClientPocTest extends EmbeddedRestAbstractTest {

    @Test
    public void httpClient_send_usage() throws URISyntaxException, IOException, InterruptedException {
        String body = "{\"name\": \"test\"}";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest
            .newBuilder()
            .headers("Content-Type", "application/json")
            .uri(new URI("http://localhost:" + port + "/entities"))
            .POST(BodyPublishers.ofString(body))
            .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, BodyHandlers.ofString());

        assertThat(httpResponse.statusCode(), is(201));
        assertThat(httpResponse.body(), is("Created"));
        assertDoesNotThrow(
            () -> UUID.fromString(httpResponse.headers().map().get("Entity-ID").get(0)));
    }
}
