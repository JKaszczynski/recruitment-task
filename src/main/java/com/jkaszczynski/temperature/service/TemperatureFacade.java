package com.jkaszczynski.temperature.service;

import com.jkaszczynski.configuration.CacheConfig;
import com.jkaszczynski.temperature.api.dto.AverageTemperatureDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemperatureFacade {

    private final TemperatureService temperatureService;
    private final FileService fileService;
    private final CacheConfig cacheConfig;
    @Setter
    private long lastModifiedTemperatureFile;

    public List<AverageTemperatureDto> getAverageTemperature(String city) {
        long currentLastModified = fileService.getLastModified();
        if (lastModifiedTemperatureFile != currentLastModified) {
            cacheConfig.clearCache();
            lastModifiedTemperatureFile = currentLastModified;
        }
        return temperatureService.getAverageTemperature(city);
    }
}
