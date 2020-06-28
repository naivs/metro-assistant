package org.naivs.perimeter.metro.assistant.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "metro")
public class MetroConfig {

    private Long pollInterval;
    private String baseUrl = "http://localhost:8080";
}
