package com.example.demo.services;

import com.example.demo.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findALL();

    List<Product> findAllActive();

    Optional<Product> findById(String id);

    Optional<Product> findByName(String name);

    Product save(Product product);

    boolean deleteById(String id);
}
