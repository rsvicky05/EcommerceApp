package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.search.ProductDocument;
import com.example.ecommerce.search.ProductSearchRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductIndexerService {

    private static final Logger logger = LoggerFactory.getLogger(ProductIndexerService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSearchRepository productSearchRepository;

    /**
     * ✅ Automatically index all products when application starts
     * This method is NOT transactional, but calls a transactional method
     */
    @EventListener(ApplicationReadyEvent.class)
    public void indexAllProductsOnStartup() {
        try {
            logger.info("🔄 Starting automatic product indexing...");
            indexAllProducts();
            logger.info("✅ Indexing completed successfully!");
        } catch (Exception e) {
            //logger.error("❌ Error during automatic indexing: {}", e.getMessage(), e);
            // Don't rethrow - let application continue even if indexing fails
        }
    }

    @Transactional  // ✅ Keeps session open for the entire method
    public void indexAllProducts() {
        logger.info(" Fetching products from database...");
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            logger.warn(" No products found in database to index.");
            return;
        }

        logger.info(" Found {} products, converting to documents...", products.size());

        List<ProductDocument> docs = products.stream()
                .map(p -> {
                    // ✅ Access category within transaction to avoid LazyInitializationException
                    Long categoryId = null;
                    if (p.getCategory() != null) {
                        categoryId = p.getCategory().getCategoryId();
                    }

                    return new ProductDocument(
                            p.getId(),
                            p.getName(),
                            p.getMrp(),
                            p.getDiscountedPrice(),
                            p.getQty(),
                            categoryId
                    );
                })
                .collect(Collectors.toList());

        logger.info("Saving {} documents to Elasticsearch...", docs.size());
        productSearchRepository.saveAll(docs);
        logger.info("Successfully indexed {} products into Elasticsearch.", docs.size());
    }

    @Transactional
    public void indexProduct(Product product) {
        Long categoryId = null;
        if (product.getCategory() != null) {
            categoryId = product.getCategory().getCategoryId();
        }

        ProductDocument doc = new ProductDocument(
                product.getId(),
                product.getName(),
                product.getMrp(),
                product.getDiscountedPrice(),
                product.getQty(),
                categoryId
        );
        productSearchRepository.save(doc);
        logger.info("✅ Indexed product: {}", product.getName());
    }

    public void deleteProductFromIndex(String productId) {
        productSearchRepository.deleteById(productId);
        logger.info("✅ Deleted product from index: {}", productId);
    }
}




// package com.example.ecommerce.service;

// import com.example.ecommerce.model.Product;
// import com.example.ecommerce.repository.ProductRepository;
// import com.example.ecommerce.search.ProductDocument;
// import com.example.ecommerce.search.ProductSearchRepository;
// import jakarta.transaction.Transactional;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.context.event.ApplicationReadyEvent;
// import org.springframework.context.event.EventListener;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.stream.Collectors;

// @Service
// public class ProductIndexerService {

//     @Autowired
//     private ProductRepository productRepository;

//     @Autowired
//     private ProductSearchRepository productSearchRepository;

//     /**
//      * ✅ Automatically index all products when application starts
//      */
//     @EventListener(ApplicationReadyEvent.class)
//     public void indexAllProductsOnStartup() {
//         System.out.println("🔄 Starting automatic product indexing...");
//         indexAllProducts();
//     }

//     @Transactional  // ✅ This keeps Hibernate session open while fetching Category
//     public void indexAllProducts() {
//         List<Product> products = productRepository.findAll();

//         if (products.isEmpty()) {
//             System.out.println("⚠️ No products found in database to index.");
//             return;
//         }

//         List<ProductDocument> docs = products.stream()
//                 .map(p -> new ProductDocument(
//                         p.getId(),
//                         p.getName(),
//                         p.getMrp(),
//                         p.getDiscountedPrice(),
//                         p.getQty(),
//                         // ✅ Safely access category now that we're inside a transaction
//                         p.getCategory() != null ? p.getCategory().getCategoryId() : null
//                 ))
//                 .collect(Collectors.toList());

//         productSearchRepository.saveAll(docs);
//         System.out.println("✅ Successfully indexed " + docs.size() + " products into Elasticsearch.");
//     }

//     /**
//      * ✅ Index a single product (useful for create/update operations)
//      */
//     @Transactional
//     public void indexProduct(Product product) {
//         ProductDocument doc = new ProductDocument(
//                 product.getId(),
//                 product.getName(),
//                 product.getMrp(),
//                 product.getDiscountedPrice(),
//                 product.getQty(),
//                 product.getCategory() != null ? product.getCategory().getCategoryId() : null
//         );
//         productSearchRepository.save(doc);
//         System.out.println("✅ Indexed product: " + product.getName());
//     }

//     /**
//      * ✅ Remove a product from index (useful for delete operations)
//      */
//     public void deleteProductFromIndex(String productId) {
//         productSearchRepository.deleteById(productId);
//         System.out.println("✅ Deleted product from index: " + productId);
//     }
// }

