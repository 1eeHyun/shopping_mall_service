package com.ldh.shoppingmall.repository.product;

import com.ldh.shoppingmall.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductName(String productName);

    List<Product> findByProductNameContainingIgnoreCase(String productName);
    @Query("SELECT p FROM Product p ORDER BY p.createdAt DESC LIMIT :limit")
    List<Product> findTopNProducts(int limit);
}
