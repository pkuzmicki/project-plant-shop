package com.example.demo.services.impl;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.services.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findALL() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findAllActive() {
        return productRepository.findByIsActiveTrue();
    }

    @Override
    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    @Override
    public Optional<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public Product save(Product product) {
        if (product.getId() == null || product.getId().isBlank()) {
            product.setId(UUID.randomUUID().toString());
            product.setActive(true);
        }
        return productRepository.save(product);
    }

    @Override
    public boolean deleteById(String id) {
        Optional<Product> productToDelete = productRepository.findById(id);
        if (productToDelete.isPresent()) {
            Product product = productToDelete.get();
            product.setActive(false);
            productRepository.save(product);
            return true;
        }
        return false;
    }
}
