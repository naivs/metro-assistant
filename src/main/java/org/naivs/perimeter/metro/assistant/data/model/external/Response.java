package org.naivs.perimeter.metro.assistant.data.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Response {

    private boolean success;
    private Page data;
    private List<Object> errors;

    @JsonProperty("human_message")
    private String humanMessage;
}
