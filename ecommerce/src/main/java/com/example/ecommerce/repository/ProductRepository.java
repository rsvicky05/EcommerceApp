package com.example.ecommerce.repository;

import org.springframework.data.domain.Page;
import com.example.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ✅ Use the entity field name `id` of Category
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category")
    List<Product> findByCategoryId(Long categoryId);
    Page<Product> findByCategoryId(Long categoryId, org.springframework.data.domain.Pageable pageable);


}
