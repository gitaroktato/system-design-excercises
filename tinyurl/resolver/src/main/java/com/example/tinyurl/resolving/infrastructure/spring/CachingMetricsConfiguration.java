package com.example.tinyurl.resolving.infrastructure.spring;

import org.springframework.boot.actuate.metrics.cache.CacheMetricsRegistrar;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CachingMetricsConfiguration {

    private final CacheMetricsRegistrar cacheMetricsRegistrar;
    private final CacheManager cacheManager;

    public CachingMetricsConfiguration(CacheMetricsRegistrar cacheMetricsRegistrar,
                                       CacheManager cacheManager) {
        this.cacheMetricsRegistrar = cacheMetricsRegistrar;
        this.cacheManager = cacheManager;
        Cache URLs = this.cacheManager.getCache("URLs");
        this.cacheMetricsRegistrar.bindCacheToRegistry(URLs);
    }
}
