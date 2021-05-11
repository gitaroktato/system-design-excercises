package com.example.tinyurl.resolving.application;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import com.example.tinyurl.resolving.infrastructure.riak.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.Exceptions;

@Component
public class ResolvingAction {

    @Autowired
    private UrlRepository repo;

    public URI doResolve(String hashKey)
            throws ExecutionException, InterruptedException, URISyntaxException {
        var result = repo.resolve(hashKey);
        return new URI(result);
    }
}
