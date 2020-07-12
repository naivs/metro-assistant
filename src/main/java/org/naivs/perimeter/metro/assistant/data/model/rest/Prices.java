package org.naivs.perimeter.metro.assistant.data.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Prices {

    private Float price;
    private String type;
    private List<PriceLevel> levels;

    @JsonProperty("is_promo")
    private boolean isPromo;
}
