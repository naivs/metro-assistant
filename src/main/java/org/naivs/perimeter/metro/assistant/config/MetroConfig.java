package org.naivs.perimeter.metro.assistant.config;

import lombok.Getter;
import lombok.Setter;
import org.naivs.perimeter.metro.assistant.service.JsonStrategy;
import org.naivs.perimeter.metro.assistant.service.MetroClient;
import org.naivs.perimeter.metro.assistant.service.ProbeStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "metro")
public class MetroConfig {

    private Long pollInterval;
    private String baseUrl = "http://localhost:8080";

    private String apiHost = "http://localhost:8080";
    private String apiBaseUrl = "api";

    @Bean
    public ProbeStrategy probeStrategy(MetroClient metroClient) {
        return new JsonStrategy(metroClient);
    }
}
