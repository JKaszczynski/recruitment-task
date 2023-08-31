package com.jkaszczynski.temperature.service;

import com.jkaszczynski.configuration.CacheConfig;
import com.jkaszczynski.temperature.api.dto.AverageTemperatureDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemperatureFacade {

    private final TemperatureService temperatureService;
    private final FileService fileService;
    private final CacheConfig cacheConfig;
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
