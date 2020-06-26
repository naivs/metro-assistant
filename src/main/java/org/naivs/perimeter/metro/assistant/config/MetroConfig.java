package org.naivs.perimeter.metro.assistant.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "metro")
public class MetroConfig {

    private Long pollInterval = 1000L * 60 * 60; // one hour
    private String baseUrl = "https://nn.metro-cc.ru";
}
