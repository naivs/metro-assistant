package org.naivs.perimeter.metro.assistant.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "bot")
public class TBotConfig {

    private String name;
    private String key;
    private List<String> recipientList = new ArrayList<>();
}
