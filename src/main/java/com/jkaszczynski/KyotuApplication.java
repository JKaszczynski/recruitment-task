package com.jkaszczynski;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KyotuApplication {

    public static void main(String[] args) {
        SpringApplication.run(KyotuApplication.class, args);
    }

}
