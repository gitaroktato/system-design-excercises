package com.example.tinyurl.resolving.application;

import static com.example.tinyurl.resolving.infrastructure.spring.CachingMetricsConfiguration.URL_CACHE_NAME;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.example.tinyurl.resolving.infrastructure.riak.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class CacheableResolvingAction {


    private final Cache cache;
    private final UrlRepository repo;

    @Autowired
    public CacheableResolvingAction(UrlRepository repo, CacheManager cacheManager) {
        this.repo = repo;
        this.cache = cacheManager.getCache(URL_CACHE_NAME);
    }

    public URI resolveOnlyFromCache(String hashKey, Throwable cause) {
        return Optional.ofNullable(cache.get(hashKey, URI.class))
                .orElseThrow(() -> new RuntimeException(cause));
    }

    @Cacheable(URL_CACHE_NAME)
    public URI resolve(String hashKey) {
        try {
            String result = repo.resolve(hashKey);
            return new URI(result);
        } catch (ExecutionException | InterruptedException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }
}
