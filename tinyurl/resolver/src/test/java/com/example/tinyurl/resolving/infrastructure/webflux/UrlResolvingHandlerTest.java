package com.example.tinyurl.resolving.infrastructure.webflux;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import com.example.tinyurl.resolving.infrastructure.riak.UrlRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@Tag("component")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UrlResolvingHandlerTest {

    public static final String BASE_PATH = "/v1/url/";
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private UrlRepository repo;

    @Test
    public void testResolve() throws Exception {
        var redirection = "https://google.com/map";
        var hash = "hash";
        when(repo.resolve(eq(hash)))
                .thenReturn(redirection);
        webTestClient.get().uri(BASE_PATH + hash)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.PERMANENT_REDIRECT)
                .expectHeader().valueEquals("Location", redirection);
    }

    @Test
    public void testResolve_withMalformedUrl() throws Exception {
        var redirection = "bad URL";
        var hash = "hash";
        when(repo.resolve(eq(hash)))
                .thenReturn(redirection);
        webTestClient.get().uri(BASE_PATH + hash)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testResolve_withInterrupt() throws Exception {
        var hash = "hash";
        when(repo.resolve(eq(hash)))
                .thenThrow(InterruptedException.class);
        webTestClient.get().uri(BASE_PATH + hash)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testResolve_withExecutionException() throws Exception {
        var hash = "hash";
        when(repo.resolve(eq(hash)))
                .thenThrow(ExecutionException.class);
        webTestClient.get().uri(BASE_PATH + hash)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}