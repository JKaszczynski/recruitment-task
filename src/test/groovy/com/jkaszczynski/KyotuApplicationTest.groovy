package com.jkaszczynski

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(classes = KyotuApplication.class)
class KyotuApplicationTest extends Specification {
    @Autowired
    KyotuApplication kyotuApplication


    def "test context loads"() {
        expect:
        kyotuApplication
    }
}
