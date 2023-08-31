package com.jkaszczynski.temperature.service


import com.jkaszczynski.configuration.CacheConfig
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
    FileService fileService = Mock()

    @Autowired
    CacheManager cacheManager

    def setup() {
        cacheManager.getCache(CacheConfig.TEMPERATURE_CACHE).clear()
    }

    def "should return properly calculated list of yearly average temperatures"() {
        given:
        Map<String, FileService.YearlyData> temperatureData = new HashMap<>()
        temperatureData.put("2020", new FileService.YearlyData(10, 2))
        fileService.readTemperatureData(_ as String) >> temperatureData

        when:
        def response = temperatureService.getAverageTemperature("Warszawa")

        then:
        response
        response.size() > 0

        and:
        response.get(0).averageTemperature() == 5
    }

    def "should take data from cache"() {
        given:
        Map<String, FileService.YearlyData> temperatureData = new HashMap<>()
        temperatureData.put("2020", new FileService.YearlyData(10, 2))
        fileService.readTemperatureData(_ as String) >> temperatureData

        and:
        String city = "Warszawa"

        when:
        temperatureService.getAverageTemperature(city)
        temperatureService.getAverageTemperature(city)

        then:
        1 * fileService.readTemperatureData(_ as String) >> temperatureData
    }
}
