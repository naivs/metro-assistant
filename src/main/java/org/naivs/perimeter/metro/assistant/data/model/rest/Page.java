package org.naivs.perimeter.metro.assistant.data.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Page {

    @JsonProperty("current_page")
    private Integer currentPage;
    private List<Product> data;
}
