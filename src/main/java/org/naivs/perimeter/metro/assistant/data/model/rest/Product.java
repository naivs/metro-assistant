package org.naivs.perimeter.metro.assistant.data.model.rest;

import lombok.Data;

import java.util.List;

@Data
public class Product {

    private Long id;
    private String name;
    private List<String> images;
    private String url;
    private List<String> barcodes;
    private Packing packing;
    private Stock stock;
    private Prices prices;
}
