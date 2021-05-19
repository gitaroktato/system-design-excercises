package com.example.tinyurl.resolving.application;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import com.example.tinyurl.resolving.infrastructure.riak.UrlRepository;
import io.micrometer.core.instrument.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import reactor.core.Exceptions;

@Component
public class ResolvingAction {

    private final UrlRepository repo;
    private final Counter counter;

    @Autowired
    public ResolvingAction(UrlRepository repo, Counter counter) {
        this.repo = repo;
        this.counter = counter;
    }

    public URI doResolve(String hashKey)
            throws ExecutionException, InterruptedException, URISyntaxException {
        var result = repo.resolve(hashKey);
        counter.increment();
        return new URI(result);
    }
}
