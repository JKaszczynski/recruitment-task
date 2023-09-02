package com.jkaszczynski.temperature.service;

import com.jkaszczynski.configuration.CacheConfig;
import com.jkaszczynski.temperature.api.dto.AverageTemperatureDto;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class TemperatureService {

    private final FileService fileService;
    private final CacheManager cacheManager;

    @Cacheable(CacheConfig.TEMPERATURE_CACHE)
    public List<AverageTemperatureDto> getAverageTemperature(@SuppressWarnings("unused") String city) {
        return Collections.emptyList();
    }

    public void populateCache() {
        var temperatureData = fileService.readTemperatureData();
        temperatureData.keySet()
                .forEach(k -> calculateAverageTemperatureAndSaveToCache(k, temperatureData.get(k).temperatureByYear()));
    }

    private void calculateAverageTemperatureAndSaveToCache(String city, Map<String, FileService.YearlyData> temperatureDataByYear) {
        var yearlyAverageTemperature = temperatureDataByYear.keySet().stream()
                .sorted()
                .map(k -> calculateAverageTemperature(k, temperatureDataByYear.get(k)))
                .toList();

        saveToCache(city, yearlyAverageTemperature);
    }

    private AverageTemperatureDto calculateAverageTemperature(String year, FileService.YearlyData temperatureData) {
        double averageTemperature = BigDecimal.valueOf(temperatureData.temperatureSum() / temperatureData.days())
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        return new AverageTemperatureDto(year, averageTemperature);
    }

    private void saveToCache(String city, List<AverageTemperatureDto> averageTemperaturesByYear) {
        var cache = cacheManager.getCache(CacheConfig.TEMPERATURE_CACHE);
        if (cache != null) {
            cache.put(city, averageTemperaturesByYear);
        }
    }
}
