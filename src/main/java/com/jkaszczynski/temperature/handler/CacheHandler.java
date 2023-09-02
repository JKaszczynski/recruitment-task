package com.jkaszczynski.temperature.handler;

import com.jkaszczynski.configuration.CacheConfig;
import com.jkaszczynski.temperature.service.FileService;
import com.jkaszczynski.temperature.service.TemperatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class CacheHandler {

    private final FileService fileService;
    private final TemperatureService temperatureService;
    private final CacheConfig cacheConfig;

    private long lastModifiedTemperatureFile;

    @EventListener(value = ApplicationReadyEvent.class)
    public void prePopulateCache() {
        log.info("Pre populating data into cache...");
        temperatureService.populateCache();
        lastModifiedTemperatureFile = fileService.getLastModified();
        log.info("Finished pre populating data into cache.");
    }

    @Scheduled(fixedDelay = 60, timeUnit = TimeUnit.SECONDS)
    public void updateCacheIfChangesInFile() {
        if (lastModifiedTemperatureFile != fileService.getLastModified()) {
            log.info("Temperature file has changed. Updating cache data...");
            cacheConfig.cacheManager();
            temperatureService.populateCache();
            lastModifiedTemperatureFile = fileService.getLastModified();
            log.info("Finished updating cache data.");
        }
    }
}
