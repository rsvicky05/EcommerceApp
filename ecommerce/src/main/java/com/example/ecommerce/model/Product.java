package com.example.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private BigDecimal mrp;
    private BigDecimal discountedPrice;
    private int qty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id")
    @JsonBackReference
    private Category category;

    public Product() {}

    public Product(String name, BigDecimal mrp, BigDecimal discountedPrice, int qty) {
        this.name = name;
        this.mrp = mrp;
        this.discountedPrice = discountedPrice;
        this.qty = qty;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; } // ✅ Added missing setter

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getMrp() { return mrp; }
    public void setMrp(BigDecimal mrp) { this.mrp = mrp; }

    public BigDecimal getDiscountedPrice() { return discountedPrice; }
    public void setDiscountedPrice(BigDecimal discountedPrice) { this.discountedPrice = discountedPrice; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
