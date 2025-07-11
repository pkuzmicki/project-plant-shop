package com.example.demo.services;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public interface CartService {
    Map<Product, Integer> findById(String id);

    Map<String, Integer> findByUserId(String userId);

    Optional<Cart> findCartByUserId(String userId);

    BigDecimal getPriceByUserId(String userId);

    Cart save(String userId, String productId, Integer amount);

    boolean deleteById(String userId, String productId);
}
