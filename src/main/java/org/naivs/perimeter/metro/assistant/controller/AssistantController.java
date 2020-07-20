package org.naivs.perimeter.metro.assistant.controller;

import lombok.RequiredArgsConstructor;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class AssistantController {

    private final ProductService productService;

    @GetMapping
    public String products(Model model) {
        List<ProductEntity> products = productService.getProducts();
        model.addAttribute("products", products);
        return "main";
    }

    @PostMapping
    public String addProduct(@RequestParam String productUrl) {
        productService.addProduct(productUrl);
        return "redirect:/";
    }

    @GetMapping("/poll/{productId}")
    public String pollProduct(@PathVariable("productId") Long productId) {
        productService.pollProduct(productId);
        return "redirect:/";
    }

    @GetMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") Long productId) {
        productService.delete(productId);
        return "redirect:/";
    }
}
