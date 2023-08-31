package com.jkaszczynski.temperature.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class FileService {

    @Value("${kyotu.temperature.file-path}")
    private String filePath;

    public Map<String, YearlyData> readTemperatureData(String city) {
        Map<String, YearlyData> temperatureByYear = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                readLine(line, city, temperatureByYear);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error with reading temperature data file");
        }
        return temperatureByYear;
    }

    private void readLine(String line, String city, Map<String, YearlyData> temperatureByYear) {
        try {
            String[] fields = line.split(";");
            String cityInRecord = fields[0];
            if (city.equals(cityInRecord)) {
                updateTemperatureData(fields, temperatureByYear);
            }
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
        yearlyData.setTemperatureSum(yearlyData.getTemperatureSum() + temperature);
        yearlyData.setDays(yearlyData.getDays() + 1);
        return yearlyData;
    }

    public Long getLastModified() {
        File file = new File(filePath);
        return file.lastModified();
    }

    @Data
    @AllArgsConstructor
    public static class YearlyData {
        private double temperatureSum;
        private int days;
    }
}