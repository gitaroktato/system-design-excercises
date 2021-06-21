package com.example.tinyurl.resolving.infrastructure.spring;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResilienceConfiguration {


    @Autowired
    private MeterRegistry meterRegistry;

    @Bean
    public CircuitBreakerRegistry geCircuitBreakerRegistry() {
        CircuitBreakerRegistry circuitBreakerRegistry =
                CircuitBreakerRegistry.ofDefaults();
        TaggedCircuitBreakerMetrics
                .ofCircuitBreakerRegistry(circuitBreakerRegistry)
                .bindTo(meterRegistry);
        return circuitBreakerRegistry;
    }

    @Bean
    public CircuitBreaker getCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("resolver");
    }
}
