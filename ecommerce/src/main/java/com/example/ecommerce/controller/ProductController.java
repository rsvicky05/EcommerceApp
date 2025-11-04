package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.search.ProductDocument;
import com.example.ecommerce.service.ProductSearchService;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/*
    Added Product API controller to manage the request and responses
 */

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductSearchService productSearchService;

    // ✅ Add new product under a category
    @PostMapping("/add/{categoryId}")
    public ResponseEntity<String> addProduct(@PathVariable Long categoryId, @RequestBody Product product) {
        return ResponseEntity.ok(productService.addProduct(product, categoryId));
    }

    // ✅ Get all products
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // ✅ Get products by category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<Product>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {

        Page<Product> products = productService.getProductsByCategory(categoryId, page, size);
        return ResponseEntity.ok(products);
    }


    // ✅ Paginated products
    @GetMapping("/page")
    public ResponseEntity<Page<Product>> getPaginatedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(productService.getPaginatedProducts(page, size));
    }

    // ✅ Delete product
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully!");
    }

    // ✅ Full-text search (Elasticsearch)
    @GetMapping("/search")
    public ResponseEntity<List<ProductDocument>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(productSearchService.searchProducts(keyword));
    }
}
