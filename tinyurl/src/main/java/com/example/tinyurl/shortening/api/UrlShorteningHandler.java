package com.example.tinyurl.shortening.api;

import java.net.MalformedURLException;
import java.net.URL;

import com.example.tinyurl.shortening.api.dto.UrlDto;
import com.example.tinyurl.shortening.api.dto.UrlShortenedDto;
import com.example.tinyurl.shortening.service.UrlShortener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UrlShorteningHandler {

    @Autowired
    private UrlShortener shortener;

    Mono<ServerResponse> shorten(ServerRequest request) {
        var apiKey = request.queryParam("apiKey");
        var url = request.bodyToMono(UrlDto.class);
        Mono<UrlShortenedDto> shortened = url.map(
                dto -> {
                    try {
                        var result = shortener.shorten(new URL(dto.originalUrl));
                        return new UrlShortenedDto(dto.originalUrl, result.toString());
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                });
        return ServerResponse.ok()
                .header("apiKey", apiKey.get())
                .body(shortened, UrlShortenedDto.class);
    }

}
