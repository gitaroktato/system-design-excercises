package com.example.tinyurl.shortening.api;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Function;

import javax.validation.constraints.NotNull;

import com.example.tinyurl.shortening.api.dto.UrlDto;
import com.example.tinyurl.shortening.api.dto.UrlShortenedDto;
import com.example.tinyurl.shortening.service.UrlShortener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

@Component
public class UrlShorteningHandler {

    @Autowired
    private UrlShortener shortener;

    Mono<ServerResponse> shorten(ServerRequest request) {
        var apiKey = request.queryParam("apiKey");
        var url = request.bodyToMono(UrlDto.class);
        return url.map(this::doShorten)
                .flatMap(shortened -> ServerResponse.ok()
                        .header("apiKey", apiKey.orElse("NULL"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(shortened))
                .onErrorResume(MalformedURLException.class, ex -> ServerResponse.badRequest()
                        .header("apiKey", apiKey.orElse("NULL")).build());
    }

    private UrlShortenedDto doShorten(UrlDto dto) {
        try {
            var url = new URL(dto.originalUrl);
            var result = shortener.shorten(url);
            return new UrlShortenedDto(dto.originalUrl, result.toString());
        } catch (MalformedURLException e) {
            throw Exceptions.propagate(e);
        }
    }
}
