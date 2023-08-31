package com.jkaszczynski.temperature.service;

import com.jkaszczynski.configuration.CacheConfig;
import com.jkaszczynski.temperature.api.dto.AverageTemperatureDto;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        double averageTemperature = BigDecimal.valueOf(yearlyData.temperatureSum() / yearlyData.days())
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        return new AverageTemperatureDto(key, averageTemperature);
    }
}
