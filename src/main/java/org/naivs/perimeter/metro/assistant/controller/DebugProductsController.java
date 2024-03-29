package org.naivs.perimeter.metro.assistant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.data.repo.ProductRepository;
import org.naivs.perimeter.metro.assistant.service.BotService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class DebugProductsController {

    private final ProductRepository productRepository;
    private final BotService botService;

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

    @GetMapping(value = "products/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductEntity getProduct() {
        ProductEntity productEntity = productRepository.findAll().get(0);
        return productEntity;
    }

    @GetMapping(value = "bot/send")
    public ResponseEntity<String> sendTelegramMessage(@RequestParam("message") String message) {
        try {
            botService.sendMessage(message);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
