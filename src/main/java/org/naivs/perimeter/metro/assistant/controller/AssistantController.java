package org.naivs.perimeter.metro.assistant.controller;

import lombok.RequiredArgsConstructor;
import org.naivs.perimeter.metro.assistant.config.MetroConfig;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class AssistantController {

    private final ProductService productService;
    private final MetroConfig metroConfig;

    @GetMapping
    public String products(Model model) {
        List<ProductEntity> products = productService.getProducts();
        model.addAttribute("products", products);
        model.addAttribute("host", metroConfig.getBaseUrl());
        return "main";
    }

    @PostMapping
    public String addProduct(@RequestParam String productUrl) {
        productService.addProduct(productUrl);
        return "redirect:/";
    }
}
