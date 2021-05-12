package com.example.tinyurl.shortening.application;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

import com.example.tinyurl.shortening.application.dto.UrlDto;
import com.example.tinyurl.shortening.application.dto.UrlShortenedDto;
import com.example.tinyurl.shortening.domain.Url;
import com.example.tinyurl.shortening.domain.UrlShortener;
import com.example.tinyurl.shortening.infrastructure.riak.UrlRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShorteningAction {

    private final Counter counter;
    private final UrlShortener shortener;
    private final UrlRepository repo;

    @Autowired
    public ShorteningAction(UrlShortener shortener, UrlRepository repo, Counter counter) {
        this.shortener = shortener;
        this.repo = repo;
        this.counter = counter;
    }

    public UrlShortenedDto doShortening(UrlDto dto)
            throws MalformedURLException, ExecutionException, InterruptedException {
        var result = shortener.shorten(Url.from(dto.originalUrl));
        repo.save(result.getHashKey(), dto.originalUrl);
        counter.increment();
        return new UrlShortenedDto(dto.originalUrl, result.toString());
    }
}
