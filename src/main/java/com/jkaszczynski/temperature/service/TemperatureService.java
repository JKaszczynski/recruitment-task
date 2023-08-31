package com.jkaszczynski.temperature.service;

import com.jkaszczynski.configuration.CacheConfig;
import com.jkaszczynski.temperature.controller.dto.AverageTemperatureDto;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TemperatureService {

    private final FileService fileService;

    @Cacheable(CacheConfig.TEMPERATURE_CACHE)
    public List<AverageTemperatureDto> getAverageTemperature(String city) {
        var temperatureData = fileService.readTemperatureData(city);
        return temperatureData.keySet().stream()
                .sorted()
                .map(k -> calculateAverageTemperature(temperatureData.get(k), k))
                .collect(Collectors.toList());
    }

    private AverageTemperatureDto calculateAverageTemperature(FileService.YearlyData yearlyData, String key) {
        return new AverageTemperatureDto(key, yearlyData.getTemperatureSum() / yearlyData.getDays());
    }
}
