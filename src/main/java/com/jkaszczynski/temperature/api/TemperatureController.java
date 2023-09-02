package com.jkaszczynski.temperature.api;

import com.jkaszczynski.temperature.api.dto.AverageTemperatureDto;
import com.jkaszczynski.temperature.service.TemperatureService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TemperatureController {

    private final TemperatureService temperatureService;

    @GetMapping(path = "/temperature/average/{city}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<AverageTemperatureDto> getAverageTemperature(@PathVariable String city) {
        return temperatureService.getAverageTemperature(city);
    }
}
