package org.naivs.perimeter.metro.assistant.data.model.external;

import lombok.Data;

@Data
public class Packing {

    private Integer size;
    private String type;

    @Override
    public String toString() {
        return "ME: " + size + " " + type;
    }
}
