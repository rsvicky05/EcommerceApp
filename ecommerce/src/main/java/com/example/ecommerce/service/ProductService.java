package com.example.ecommerce.service;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // ✅ Add Product
    public String addProduct(Product product, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElse(null);

        if (category == null) {
            return "Category not found!";
        }

        // Assign the category to the product
        product.setCategory(category);

        // ✅ No need to calculate discounted price — user provides it directly
        productRepository.save(product);

        return "Product added successfully!";
    }

    // ✅ Fetch all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ✅ Fetch products by category
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    // ✅ Fetch products by category with pagination
    public Page<Product> getProductsByCategory(Long categoryId, int page, int size) {
        return productRepository.findByCategoryId(categoryId, PageRequest.of(page, size));
    }

    // ✅ Pagination support
    public Page<Product> getPaginatedProducts(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }

    // ✅ Delete Product
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}