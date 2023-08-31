package com.jkaszczynski.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String TEMPERATURE_CACHE = "temperatureCache";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(TEMPERATURE_CACHE);
    }

    @CacheEvict(value = TEMPERATURE_CACHE, allEntries = true)
    public void clearCache() {
    }
}
