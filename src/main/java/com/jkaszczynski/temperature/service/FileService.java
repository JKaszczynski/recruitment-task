package com.jkaszczynski.temperature.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileService {

    @Value("${kyotu.temperature.file.path}")
    private String filePath;

    public Map<String, CityTemperatureData> readTemperatureData() {
        Map<String, CityTemperatureData> citiesTemperatures = new ConcurrentHashMap<>();
        try (Stream<String> lines = Files.lines(Path.of(filePath))) {
            lines.parallel().forEach(l -> readLine(l, citiesTemperatures));
        } catch (IOException e) {
            throw new RuntimeException("Error with reading temperature data file: " + e.getMessage());
        }
        return citiesTemperatures;
    }

    private void readLine(String line, Map<String, CityTemperatureData> temperatureByYear) {
        try {
            String[] fields = line.split(";");
            String city = fields[0];
            temperatureByYear.computeIfAbsent(city, (__) -> new CityTemperatureData(new ConcurrentHashMap<>()));
            updateTemperatureData(fields, temperatureByYear.get(city).temperatureByYear());
        } catch (Exception e) {
            log.warn("Could not read malformed line: " + e.getMessage());
        }
    }

    private void updateTemperatureData(String[] fields, Map<String, YearlyData> temperatureByYear) {
        String year = fields[1].split("-")[0];
        double temperature = Double.parseDouble(fields[2]);
        temperatureByYear.merge(year, new YearlyData(temperature, 1),
                (avg, __) -> updateYearlyData(avg, temperature));
    }

    private YearlyData updateYearlyData(YearlyData yearlyData, double temperature) {
        return new YearlyData(yearlyData.temperatureSum() + temperature, yearlyData.days() + 1);
    }

    public long getLastModified() {
        File file = new File(filePath);
        return file.lastModified();
    }

    public record CityTemperatureData(Map<String, YearlyData> temperatureByYear) {
    }

    public record YearlyData(double temperatureSum, int days) {
    }
}
