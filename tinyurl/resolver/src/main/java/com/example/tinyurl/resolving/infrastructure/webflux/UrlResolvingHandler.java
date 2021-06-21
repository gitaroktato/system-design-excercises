package com.example.tinyurl.resolving.infrastructure.webflux;

import java.net.URI;
import java.net.URISyntaxException;

import com.example.tinyurl.resolving.application.ResolvingAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class UrlResolvingHandler {

    @Autowired
    private ResolvingAction resolvingAction;

    public Mono<ServerResponse> resolve(ServerRequest request) {
        var hashKey = request.pathVariable("hash");
        return resolveAsync(hashKey)
                .flatMap(location -> ServerResponse.permanentRedirect(location).build())
                .onErrorResume(URISyntaxException.class, ex -> ServerResponse.badRequest().build())
                .onErrorResume(NullPointerException.class, ex -> ServerResponse.notFound().build());
    }

    private Mono<URI> resolveAsync(String hashKey) {
        return Mono.fromCallable(() -> resolvingAction.resolve(hashKey))
                .subscribeOn(Schedulers.boundedElastic());
    }

}
