package org.naivs.perimeter.metro.assistant.service;

import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;

public interface ProbeStrategy {

    boolean probe(ProductEntity product);
}
