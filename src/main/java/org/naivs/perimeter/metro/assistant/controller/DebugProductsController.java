package org.naivs.perimeter.metro.assistant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api")
public class DebugProductsController {

    @GetMapping(value = "products", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getProduct(
            @RequestParam("slug") String slug
    ) {
        if (slug != null && !slug.isEmpty()) {
            Resource resource = new ClassPathResource("/debug-responses/"+ slug + ".json");
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(resource.getInputStream(), Object.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}