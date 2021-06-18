package com.example.tinyurl.resolving.infrastructure.spring;

import org.springframework.boot.actuate.metrics.cache.CacheMetricsRegistrar;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CachingConfiguration {

    public static final String URL_CACHE_NAME = "URLs";
    private final CacheMetricsRegistrar cacheMetricsRegistrar;
    private final CacheManager cacheManager;

    public CachingConfiguration(CacheMetricsRegistrar cacheMetricsRegistrar,
                                CacheManager cacheManager) {
        this.cacheMetricsRegistrar = cacheMetricsRegistrar;
        this.cacheManager = cacheManager;
        Cache URLs = this.cacheManager.getCache("URLs");
        this.cacheMetricsRegistrar.bindCacheToRegistry(URLs);
    }

    @Bean
    public Cache getCache() {
        return cacheManager.getCache(URL_CACHE_NAME);
    }
}
