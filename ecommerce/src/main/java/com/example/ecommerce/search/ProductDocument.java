package com.example.ecommerce.search;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;


@Builder
@Document(indexName = "products")
public class ProductDocument {

    @Id
    private Long id;
    private String name;
    private BigDecimal mrp;
    private BigDecimal discountedPrice;
    private int qty;
    private Long categoryId;

    // ✅ Default constructor (required by Spring)
    public ProductDocument() {}

    // Optional: parameterized constructor for convenience
    public ProductDocument(Long id, String name, BigDecimal mrp, BigDecimal discountedPrice, int qty, Long catId) {
        this.id = id;
        this.name = name;
        this.mrp = mrp;
        this.discountedPrice = discountedPrice;
        this.qty = qty;
        this.categoryId = catId;
    }

    // Getters & Setters
    public Long getPId() { return id; }
    public void setPId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getMrp() { return mrp; }
    public void setMrp(BigDecimal mrp) { this.mrp = mrp; }

    public BigDecimal getDiscountedPrice() { return discountedPrice; }
    public void setDiscountedPrice(BigDecimal discountedPrice) { this.discountedPrice = discountedPrice; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}
