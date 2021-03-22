package com.example.demo.resolve.api;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/{hash}")
public class UrlResolve {

    @GetMapping
    public Mono<ResponseEntity<Void>> resolve(@PathVariable String hash) throws URISyntaxException {
        var headers = new HttpHeaders();
        headers.setLocation(new URI("https://google.com/" + hash));
        return Mono.just(ResponseEntity.status(HttpStatus.FOUND).headers(headers).build());
    }
}
