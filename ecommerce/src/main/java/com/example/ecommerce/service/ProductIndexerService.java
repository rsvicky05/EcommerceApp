package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.search.ProductDocument;
import com.example.ecommerce.search.ProductSearchRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductIndexerService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSearchRepository productSearchRepository;

    @Transactional  // ✅ This keeps Hibernate session open while fetching Category
    public void indexAllProducts() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            System.out.println("⚠️ No products found in database to index.");
            return;
        }

        List<ProductDocument> docs = products.stream()
                .map(p -> new ProductDocument(
                        p.getId(),
                        p.getName(),
                        p.getMrp(),
                        p.getDiscountedPrice(),
                        p.getQty(),
                        // ✅ Safely access category now that we’re inside a transaction
                        p.getCategory() != null ? p.getCategory().getCategoryId() : null
                ))
                .collect(Collectors.toList());

        productSearchRepository.saveAll(docs);
        //System.out.println("✅ Indexed " + docs.size() + " products into Elasticsearch.");
    }
}
