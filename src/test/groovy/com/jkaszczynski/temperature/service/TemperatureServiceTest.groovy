package com.jkaszczynski.temperature.service

import com.jkaszczynski.configuration.CacheConfig
import com.jkaszczynski.temperature.api.dto.AverageTemperatureDto
import com.jkaszczynski.temperature.handler.CacheHandler
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest
@EnableCaching
class TemperatureServiceTest extends Specification {

    @Subject
    @Autowired
    TemperatureService temperatureService

    @SpringBean
    CacheHandler cacheHandler = Mock()

    @SpringBean
    FileService fileService = Mock()

    @Autowired
    CacheManager cacheManager

    def setup() {
        cacheManager.getCache(CacheConfig.TEMPERATURE_CACHE).clear()
    }

    def "should properly cache data with list of cities with yearly average temperatures"() {
        given:
        Map<String, FileService.CityTemperatureData> cityData = new HashMap<>()
        Map<String, FileService.YearlyData> yearlyData = new HashMap<>()
        yearlyData.put("2020", new FileService.YearlyData(10, 2))
        cityData.put("Warszawa", new FileService.CityTemperatureData(yearlyData))
        fileService.readTemperatureData() >> cityData

        when:
        temperatureService.populateCache()

        then:
        cacheManager.getCache(CacheConfig.TEMPERATURE_CACHE)
        cacheManager.getCache(CacheConfig.TEMPERATURE_CACHE).get("Warszawa")

        and:
        def cache = cacheManager.getCache(CacheConfig.TEMPERATURE_CACHE).get("Warszawa").get() as List<AverageTemperatureDto>
        cache.get(0).year() == "2020"
        cache.get(0).averageTemperature() == 5

    }

    def "should take data from cache"() {
        given:
        String city = "Warszawa"

        and:
        cacheManager.getCache(CacheConfig.TEMPERATURE_CACHE).put(city, List.of(new AverageTemperatureDto("2020", 10.3)))

        when:
        def response = temperatureService.getAverageTemperature(city)

        then:
        response.size() > 0
    }
}
