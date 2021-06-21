package com.example.tinyurl.resolving.application;

import static com.example.tinyurl.resolving.infrastructure.spring.CachingMetricsConfiguration.URL_CACHE_NAME;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.example.tinyurl.resolving.infrastructure.riak.UrlRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.micrometer.core.instrument.Counter;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class ResolvingAction {

    private final UrlRepository repo;
    private final Counter counter;
    private final CircuitBreaker circuitBreaker;
    private final Cache cache;

    @Autowired
    public ResolvingAction(UrlRepository repo, Counter counter,
                           CircuitBreaker circuitBreaker, CacheManager cacheManager) {
        this.repo = repo;
        this.counter = counter;
        this.circuitBreaker = circuitBreaker;
        this.cache = cacheManager.getCache(URL_CACHE_NAME);
    }

    @Cacheable(URL_CACHE_NAME)
    public URI resolve(String hashKey) {
        var supplier =  circuitBreaker.decorateSupplier(() -> doResolve(hashKey));
        return Try.ofSupplier(supplier)
                .recover(throwable -> recoverUriFromCache(hashKey, throwable)).get();
    }

    private URI recoverUriFromCache(String hashKey, Throwable cause) {
        return Optional.ofNullable(cache.get(hashKey, URI.class))
                .orElseThrow(() -> new RuntimeException(cause));
    }

    private URI doResolve(String hashKey) {
        try {
            String result = repo.resolve(hashKey);
            counter.increment();
            return new URI(result);
        } catch (ExecutionException | InterruptedException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }
}
