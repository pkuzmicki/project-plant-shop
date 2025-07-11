package com.example.demo.services;

public interface PaymentService {
    String createCheckoutSession(String purchaseId);

    void handleWebhook(String payload, String signature);
}
