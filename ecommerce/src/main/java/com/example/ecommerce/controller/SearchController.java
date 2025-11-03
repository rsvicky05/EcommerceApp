package com.example.ecommerce.controller;

import com.example.ecommerce.search.ProductDocument;
import com.example.ecommerce.service.ProductSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class SearchController {

    @Autowired
    private ProductSearchService searchService;

    @GetMapping
    public List<ProductDocument> search(@RequestParam String q,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return searchService.searchProducts(q);
    }
}
