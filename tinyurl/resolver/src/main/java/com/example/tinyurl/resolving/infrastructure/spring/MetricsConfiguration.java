package com.example.tinyurl.resolving.infrastructure.spring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfiguration {

    @Autowired
    private MeterRegistry meterRegistry;

    @Bean
    public Counter shorteningCounter() {
        return Counter.builder("application.resolving")    // 2 - create a counter using the fluent API
                .register(meterRegistry);
    }
}
