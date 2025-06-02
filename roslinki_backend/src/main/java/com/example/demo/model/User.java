package com.example.demo.model;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {
    private String id;
    private String login;
    private String password;
    private String role;
}
