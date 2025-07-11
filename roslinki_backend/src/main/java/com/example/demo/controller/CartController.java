package com.example.demo.controller;

import com.example.demo.dto.CartAddRequest;
import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Map<String, Integer>> getAllProducts(@AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Użytkownik nie znaleziony: " + login));

        return ResponseEntity.ok(cartService.findByUserId(user.getId()));
    }

    @GetMapping("/price")
    public ResponseEntity<BigDecimal> getCartPrice(@AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Użytkownik nie znaleziony: " + login));

        return ResponseEntity.ok(cartService.getPriceByUserId(user.getId()));
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addProduct(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CartAddRequest cartAddRequest) {
        String login = userDetails.getUsername();
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("nie znaleziono" + login));

        Cart cart = cartService.save(user.getId(), cartAddRequest.getProductId(), cartAddRequest.getAmount());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> deleteProduct(@AuthenticationPrincipal UserDetails userDetails, @RequestBody String productId) {
        String login = userDetails.getUsername();
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("nie znaleziono" + login));

        boolean deleted = cartService.deleteById(user.getId(), productId);
        if (deleted) {
            return ResponseEntity.ok().body("usunieto z koszyka produkt o id: " + productId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("nie znaleziono produktu w koszyku");
        }
    }
}
