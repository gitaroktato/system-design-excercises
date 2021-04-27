package com.example.tinyurl.shortening.api;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import com.example.tinyurl.shortening.api.dto.UrlDto;
import com.example.tinyurl.shortening.api.dto.UrlShortenedDto;
import com.example.tinyurl.shortening.domain.UrlShortener;
import com.example.tinyurl.shortening.infrastructure.riak.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class UrlShorteningHandler {

    @Autowired
    private UrlShortener shortener;
    @Autowired
    private UrlRepository repo;

    Mono<ServerResponse> shorten(ServerRequest request) {
        var apiKey = request.queryParam("apiKey");
        var url = request.bodyToMono(UrlDto.class);
        return url.map(this::doShorten)
                .flatMap(shortened -> ServerResponse.ok()
                        .header("apiKey", apiKey.orElse("NULL"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(shortened))
                .onErrorResume(MalformedURLException.class, ex -> ServerResponse.badRequest()
                        .header("apiKey", apiKey.orElse("NULL")).build())
                .subscribeOn(Schedulers.boundedElastic());
    }

    private UrlShortenedDto doShorten(UrlDto dto) {
        try {
            var url = new URL(dto.originalUrl);
            var result = shortener.shorten(url);
            var key = result.getPath().substring(1);
            repo.save(key, dto.originalUrl);
            return new UrlShortenedDto(dto.originalUrl, result.toString());
        } catch (MalformedURLException | ExecutionException | InterruptedException e) {
            throw Exceptions.propagate(e);
        }
    }
}
