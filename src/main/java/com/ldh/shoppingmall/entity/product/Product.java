package com.ldh.shoppingmall.entity.product;

import com.ldh.shoppingmall.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private BigDecimal price;

    public Product() {
    }

    public Product(String productName, String description, String imageUrl, BigDecimal price) {
        this.productName = productName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
    }
}
