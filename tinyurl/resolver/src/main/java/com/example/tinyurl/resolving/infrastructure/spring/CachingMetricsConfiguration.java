package com.example.tinyurl.resolving.infrastructure.spring;

import org.springframework.boot.actuate.metrics.cache.CacheMetricsRegistrar;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CachingMetricsConfiguration {

    public static final String URL_CACHE_NAME = "URLs";
    private final CacheMetricsRegistrar cacheMetricsRegistrar;
    private final CacheManager cacheManager;

    public CachingMetricsConfiguration(CacheMetricsRegistrar cacheMetricsRegistrar,
                                       CacheManager cacheManager) {
        this.cacheMetricsRegistrar = cacheMetricsRegistrar;
        this.cacheManager = cacheManager;
        Cache URLs = cacheManager.getCache(URL_CACHE_NAME);
        this.cacheMetricsRegistrar.bindCacheToRegistry(URLs);
    }
}
