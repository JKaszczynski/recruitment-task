package com.jkaszczynski.temperature.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject


@SpringBootTest
class FileServiceTest extends Specification {

    @Subject
    @Autowired
    FileService fileService

    def "should return map of yearly temperatures"() {
        when:
        def response = fileService.readTemperatureData("Warszawa")

        then:
        response
        response.size() > 0
        response.get("2018").days() > 0
        response.get("2019").days() > 0
        response.get("2018").temperatureSum() > 0
        response.get("2019").temperatureSum() > 0
    }

    def "should return last modified time of file"() {
        when:
        def response = fileService.getLastModified()

        then:
        response
        response > 0
    }
}
