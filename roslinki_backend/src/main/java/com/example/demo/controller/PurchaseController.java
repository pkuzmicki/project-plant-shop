package com.example.demo.controller;

import com.example.demo.dto.StatusChangeRequest;
import com.example.demo.model.Cart;
import com.example.demo.model.Purchase;
import com.example.demo.model.User;
import com.example.demo.repository.PurchaseRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.CartService;
import com.example.demo.services.PurchaseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchase")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final CartService cartService;

    @PostMapping("/order")
    public ResponseEntity<Purchase> orderProduct(@AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Użytkownik nie znaleziony: " + login));

        Cart cart = cartService.findCartByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user: " + user.getId()));

        Purchase purchase = purchaseService.order(user.getId(), cart.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(purchase);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/status")
    public ResponseEntity<?> changeStatus(@AuthenticationPrincipal UserDetails userDetails, @RequestBody StatusChangeRequest statusChangeRequest) {
        Purchase purchase = purchaseService.findById(statusChangeRequest.getPurchaseId()).orElseThrow(() -> new UsernameNotFoundException("nie znaleziono tranzakcji"));
        purchase.setStatus(statusChangeRequest.getStatus());

        purchaseRepository.save(purchase);

        return ResponseEntity.ok().body("zmieniono status na: " + statusChangeRequest.getStatus());
    }
}
