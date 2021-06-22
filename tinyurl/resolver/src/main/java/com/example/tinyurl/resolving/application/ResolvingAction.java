package com.example.tinyurl.resolving.application;

import java.net.URI;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.micrometer.core.instrument.Counter;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResolvingAction {

    private final Counter counter;
    private final CircuitBreaker circuitBreaker;
    private final CacheableResolvingAction cacheableAction;

    @Autowired
    public ResolvingAction(CacheableResolvingAction cacheableAction, Counter counter,
                           CircuitBreaker circuitBreaker) {
        this.cacheableAction = cacheableAction;
        this.counter = counter;
        this.circuitBreaker = circuitBreaker;
    }

    public URI resolve(String hashKey) {
        var supplier =  circuitBreaker.decorateSupplier(
                () -> cacheableAction.resolve(hashKey));
        var result = Try.ofSupplier(supplier)
                .recover(throwable -> cacheableAction.resolveOnlyFromCache(hashKey, throwable))
                .get();
        counter.increment();
        return result;
    }

}
