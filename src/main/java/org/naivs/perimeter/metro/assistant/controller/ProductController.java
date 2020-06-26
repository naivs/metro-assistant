package org.naivs.perimeter.metro.assistant.controller;

import lombok.RequiredArgsConstructor;
import org.naivs.perimeter.metro.assistant.model.MetroProduct;
import org.naivs.perimeter.metro.assistant.service.ScheduledService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("metro-assistant")
public class ProductController {

    private final ScheduledService scheduledService;

    @GetMapping
    public String products(Model model) {
        List<MetroProduct> products = scheduledService.getProducts();
        model.addAttribute("products", products);
        model.addAttribute("product", "");
        return "products";
    }

    @PostMapping
    public String addProduct(@RequestParam String productUrl) {
        MetroProduct product = new MetroProduct();
        product.setUrl(productUrl);
        scheduledService.addProduct(product);
        return "redirect:/metro-assistant";
    }
}
