package org.naivs.perimeter.metro.assistant.data.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PriceHistoryModel {

    private List<String> columns = new ArrayList<>();
    private Map<String, List<Float>> rows = new HashMap<>();
}
