package com.jkaszczynski.temperature.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.jkaszczynski.configuration.CacheConfig
import com.jkaszczynski.temperature.service.FileService
import com.jkaszczynski.temperature.service.TemperatureFacade
import com.jkaszczynski.temperature.service.TemperatureService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import spock.lang.Shared
import spock.lang.Specification

@AutoConfigureMockMvc
@EnableWebMvc
@SpringBootTest(classes = [TemperatureController.class, FileService.class,
        TemperatureFacade.class, TemperatureService.class, CacheConfig.class])
class TemperatureControllerTest extends Specification {

    @Autowired
    MockMvc mvc

    @Shared
    ObjectMapper objectMapper

    def setupSpec() {
        objectMapper = new ObjectMapper()
    }

    def 'api for getting average temperature for cities returns HTTP 200 and not empty response'() {
        given:
        String city = "Warszawa"

        when:
        def response = mvc
                .perform(MockMvcRequestBuilders.get(String.format("/api/temperature/average/%s", city)))
                .andReturn().response

        then:
        response
        response.status == HttpStatus.OK.value()

        and:
        with(objectMapper.readValue(response.contentAsString, List)) {
            it.size() > 0
        }
    }
}
