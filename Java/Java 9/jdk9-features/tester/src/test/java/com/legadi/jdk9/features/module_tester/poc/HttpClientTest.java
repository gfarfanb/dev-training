package com.legadi.jdk9.features.module_tester.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpRequest.BodyProcessor;
import jdk.incubator.http.HttpResponse;
import jdk.incubator.http.HttpResponse.BodyHandler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.legadi.jdk9.features.module_tester.embedded.EmbeddedRestAbstractTest;

public class HttpClientTest extends EmbeddedRestAbstractTest {

    @Test
    public void send_Usage() throws URISyntaxException, IOException, InterruptedException {
        String body = "{\"name\": \"test\"}";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest
            .newBuilder()
            .headers("Content-Type", "application/json")
            .uri(new URI("http://localhost:" + port + "/entities"))
            .POST(BodyProcessor.fromString(body))
            .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, BodyHandler.asString());

        assertThat(httpResponse.statusCode(), is(201));
        assertThat(httpResponse.body(), is("Created"));
        assertDoesNotThrow(
            () -> UUID.fromString(httpResponse.headers().map().get("Entity-ID").get(0)));
    }
}
