package com.example.tinyurl.resolving.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import com.example.tinyurl.resolving.infrastructure.UrlResolver;
import com.example.tinyurl.resolving.infrastructure.riak.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class UrlResolvingHandler {

    @Autowired
    private UrlRepository repo;

    public Mono<ServerResponse> resolve(ServerRequest request)  {
        var hashed = request.pathVariable("hash");
        return Mono.fromCallable(() -> doResolve(hashed))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(location -> ServerResponse.permanentRedirect(location).build())
                .onErrorResume(URISyntaxException.class, ex -> ServerResponse.badRequest().build());
    }

    private URI doResolve(String hashed) {
        try {
            var result = repo.resolve(hashed);
            return new URI(result);
        } catch (ExecutionException | InterruptedException | URISyntaxException e) {
            throw Exceptions.propagate(e);
        }
    }
}
