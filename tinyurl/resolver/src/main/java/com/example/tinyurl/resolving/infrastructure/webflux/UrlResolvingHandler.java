package com.example.tinyurl.resolving.infrastructure.webflux;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.example.tinyurl.resolving.application.ResolvingAction;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class UrlResolvingHandler {

    @Autowired
    private ResolvingAction resolvingAction;

    @Autowired
    private CircuitBreaker circuitBreaker;

    @Autowired
    private Cache cache;

    public Mono<ServerResponse> resolve(ServerRequest request) {
        var hashKey = request.pathVariable("hash");
        return resolveAsync(hashKey)
                .flatMap(location -> ServerResponse.permanentRedirect(location).build())
                .onErrorResume(URISyntaxException.class, ex -> ServerResponse.badRequest().build())
                .onErrorResume(NullPointerException.class, ex -> ServerResponse.notFound().build());
    }

    private Mono<URI> resolveAsync(String hashKey) {
        return Mono.fromCallable(() -> resolveWithCircuitBreaker(hashKey))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private URI resolveWithCircuitBreaker(String hashKey) {
        var supplier =  circuitBreaker.decorateSupplier(() -> resolve(hashKey));
        return Try.ofSupplier(supplier)
                .recover(throwable -> recoverUriFromCache(hashKey, throwable)).get();
    }

    private URI recoverUriFromCache(String hashKey, Throwable cause) {
            return Optional.ofNullable(cache.get(hashKey, String.class))
                    .map(uri -> {
                        try {
                            return new URI(uri);
                        } catch (URISyntaxException e) {
                            throw Exceptions.propagate(e);
                        }
                    }).orElseThrow(() -> Exceptions.propagate(cause));
    }

    private URI resolve(String hashKey) {
        try {
            return resolvingAction.doResolve(hashKey);
        } catch (ExecutionException | InterruptedException | URISyntaxException e) {
            throw Exceptions.propagate(e);
        }
    }

}
