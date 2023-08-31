package com.jkaszczynski.temperature.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataInitializer {

    private final TemperatureFacade temperatureFacade;
    private final FileService fileService;

    @Value("${kyotu.temperature.file.init-cache}")
    private boolean prePopulateCache;

    @EventListener(value = ApplicationReadyEvent.class)
    private void prePopulateCache() {
        if (prePopulateCache) {
            log.info("Pre populating data into cache...");
            Set<String> cities = fileService.getAllCitiesFromFile();
            cities.forEach(temperatureFacade::getAverageTemperature);
            temperatureFacade.setLastModifiedTemperatureFile(fileService.getLastModified());
            log.info("Finished pre populating data into cache.");
        }
    }
}
