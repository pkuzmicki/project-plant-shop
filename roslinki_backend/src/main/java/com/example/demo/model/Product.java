package com.example.demo.model;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
@Table(name = "product")
public class Product {
    private String id;
    private String name;
    private float price;
    private String imgId;

}
