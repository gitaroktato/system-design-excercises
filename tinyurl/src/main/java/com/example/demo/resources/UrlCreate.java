package com.example.demo.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/url")
public class UrlCreate {

    @PostMapping
    Mono<ResponseEntity<Void>> create(@RequestParam String apiKey, @RequestBody UrlEntity url) {
        return Mono.just(ResponseEntity.ok().header("apiKey", apiKey)
                .build());
    }

}
