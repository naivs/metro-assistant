package org.naivs.perimeter.metro.assistant.http;

import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;

public interface ProbeStrategy {

    boolean probe(ProductEntity product);
}
