package com.example.demo.services;

import com.example.demo.model.Purchase;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface PurchaseService {

    Purchase order(String userId, String cartId);

    Optional<Purchase> findById(String id);

    Optional<Purchase> findByUserIdAndStatusIsPending(String userId);

}
