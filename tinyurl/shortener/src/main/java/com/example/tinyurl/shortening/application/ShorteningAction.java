package com.example.tinyurl.shortening.application;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

import com.example.tinyurl.shortening.application.dto.UrlDto;
import com.example.tinyurl.shortening.application.dto.UrlShortenedDto;
import com.example.tinyurl.shortening.domain.Url;
import com.example.tinyurl.shortening.domain.UrlShortener;
import com.example.tinyurl.shortening.infrastructure.riak.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShorteningAction {

    @Autowired
    private UrlShortener shortener;
    @Autowired
    private UrlRepository repo;

    public void setShortener(UrlShortener shortener) {
        this.shortener = shortener;
    }

    public void setRepository(UrlRepository repo) {
        this.repo = repo;
    }

    public UrlShortenedDto doShortening(UrlDto dto)
            throws MalformedURLException, ExecutionException, InterruptedException {
        var result = shortener.shorten(Url.from(dto.originalUrl));
        repo.save(result.getHashKey(), dto.originalUrl);
        return new UrlShortenedDto(dto.originalUrl, result.toString());
    }
}
