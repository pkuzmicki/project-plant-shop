package com.example.demo.services.impl;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.CartService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Map<Product, Integer> findById(String id) {
        return Map.of();
    }

    @Override
    public Map<String, Integer> findByUserId(String userId) {
        return cartRepository.findByUserId(userId)
                .map(Cart::getProducts)
                .orElseGet(HashMap::new);
    }

    @Override
    public Optional<Cart> findCartByUserId(String userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public BigDecimal getPriceByUserId(String userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> {
            return new EntityNotFoundException("nie znaleziono koszyka uztkownika o id: " + userId);
        });

        BigDecimal price = BigDecimal.valueOf(0);
        if (cart.getProducts().isEmpty()) {
            return price;
        } else {
            for (Map.Entry<String, Integer> entry : cart.getProducts().entrySet()) {
                price = price.add(productRepository.findById(entry.getKey()).get().getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
            }
            return price;
        }
    }

    @Override
    public Cart save(String userId, String productId, Integer amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        Optional<Cart> existingCartOpt = cartRepository.findByUserId(userId);
        Cart cart = null;

        if (existingCartOpt.isPresent()) {
            cart = existingCartOpt.get();
            Map<String, Integer> products = cart.getProducts();

            products.put(productId, products.getOrDefault(productId, 0) + amount);
            cart.setProducts(products);
        } else {
            Map<String, Integer> products = findByUserId(userId);
            products.put(productId, amount);

            cart = Cart.builder()
                .id(UUID.randomUUID().toString())
                .user(user)
                .products(products)
                .build();
        }

        return cartRepository.save(cart);
    }

    @Override
    public boolean deleteById(String userId, String productId) {
        Optional<Cart> existingCart = cartRepository.findByUserId(userId);

        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            Map<String, Integer> products = cart.getProducts();
            if (products.containsKey(productId)) {
                products.remove(productId);
                cart.setProducts(products);
                cartRepository.save(cart);
                return true;
            }
        }
        return false;
    }
}
