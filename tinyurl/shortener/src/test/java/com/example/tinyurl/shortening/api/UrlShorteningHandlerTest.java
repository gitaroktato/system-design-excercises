package com.example.tinyurl.shortening.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;

import com.example.tinyurl.shortening.api.dto.UrlDto;
import com.example.tinyurl.shortening.api.dto.UrlShortenedDto;
import com.example.tinyurl.shortening.domain.UrlShortener;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@Tag("component")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UrlShorteningHandlerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private UrlShortener shortener;

    @Test
    public void testShorten() throws MalformedURLException {
        var shortHandUrl = "https://shortened.url/value";
        when(shortener.shorten(any(URL.class)))
                .thenReturn(new URL(shortHandUrl));
        var dto = new UrlDto();
        dto.originalUrl = "https://google.com/1234";
        webTestClient.post().uri("/v1/url?apiKey=KxDB")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().valueEquals("apiKey", "KxDB")
                .expectBody(UrlShortenedDto.class)
                .value(UrlShortenedDto::getShortenedUrl, IsEqual.equalTo(shortHandUrl));
    }

    @Test
    public void testShortenWithMalformedURL() throws MalformedURLException {
        var dto = new UrlDto();
        dto.originalUrl = "mal.form.ed";
        webTestClient.post().uri("/v1/url")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is4xxClientError();
    }

}