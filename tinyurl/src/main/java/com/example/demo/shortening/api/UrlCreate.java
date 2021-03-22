package com.example.demo.shortening.api;

import java.net.MalformedURLException;
import java.net.URL;

import com.example.demo.shortening.api.dto.UrlEntity;
import com.example.demo.shortening.api.dto.UrlShortened;
import com.example.demo.shortening.domain.UrlShortener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/url")
public class UrlCreate {

    @Autowired
    private UrlShortener shortener;

    @PostMapping
    Mono<ResponseEntity<UrlShortened>> create(@RequestParam String apiKey, @RequestBody UrlEntity url) throws MalformedURLException {
        var shortened = shortener.shorten(new URL(url.originalUrl));
        return Mono.just(ResponseEntity
                .ok().header("apiKey", apiKey)
                .body(new UrlShortened(url.originalUrl, shortened.toString())));
    }

}
