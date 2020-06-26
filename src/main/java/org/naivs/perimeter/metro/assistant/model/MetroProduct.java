package org.naivs.perimeter.metro.assistant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MetroProduct {

    private Long id;
    private String name;
    private String url;
    private String me;
    private Float regularPrice;
    private Map<Integer, Float> wholesalePrice;
    private Integer leftPct;
}
