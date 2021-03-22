package com.example.demo.resources;

import com.example.demo.resources.dto.UrlEntity;
import com.example.demo.resources.dto.UrlShortened;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/url")
public class UrlCreate {

    @PostMapping
    Mono<ResponseEntity<UrlShortened>> create(@RequestParam String apiKey, @RequestBody UrlEntity url) {
        return Mono.just(ResponseEntity
                .ok().header("apiKey", apiKey)
                .body(new UrlShortened(url.originalUrl, "https://localhost/xaBcdf")));
    }

}
