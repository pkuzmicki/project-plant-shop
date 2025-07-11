package com.example.demo.services.impl;

import com.example.demo.model.Purchase;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.PurchaseRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;

    @Override
    public Purchase order(String userId, String cartId) {

        Purchase purchase = Purchase.builder()
                .id(UUID.randomUUID().toString())
                .user(userRepository.findById(userId).orElseThrow())
                .cart(cartRepository.findByUserId(userId).orElseThrow())
                .purchase_date(LocalDateTime.now())
                .status("PENDING")
                .build();

        return purchaseRepository.save(purchase);
    }

    @Override
    public Optional<Purchase> findById(String id) {
        return purchaseRepository.findById(id);
    }

    @Override
    public Optional<Purchase> findByUserIdAndStatusIsPending(String userId) {
        return purchaseRepository.findByUserIdAndStatus(userId, "PENDING");
    }


}
