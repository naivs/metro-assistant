package org.naivs.perimeter.metro.assistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MetroAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetroAssistantApplication.class, args);
    }

}
