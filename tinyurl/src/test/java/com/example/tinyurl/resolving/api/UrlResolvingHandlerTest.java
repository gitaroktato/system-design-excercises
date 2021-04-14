package com.example.tinyurl.resolving.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import com.example.tinyurl.resolving.service.UrlResolver;
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

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private UrlResolver resolver;

    @Test
    public void testResolve() throws URISyntaxException {
        var redirection = "https://google.com/map";
        when(resolver.getRedirection(any()))
                .thenReturn(new URI(redirection));
        webTestClient.get().uri("/hash")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.PERMANENT_REDIRECT)
                .expectHeader().valueEquals("Location", redirection);
    }
}