package com.example.demo.services.impl;

import com.example.demo.model.Payment;
import com.example.demo.model.Purchase;
import com.example.demo.model.Status;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.PurchaseRepository;
import com.example.demo.services.CartService;
import com.example.demo.services.PaymentService;
import com.example.demo.services.PurchaseService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PurchaseService purchaseService;
    private final PurchaseRepository purchaseRepository;
    private final PaymentRepository paymentRepository;
    private final CartService cartService;

    @org.springframework.beans.factory.annotation.Value("${STRIPE_API_KEY}")
    private String apiKey;
    @org.springframework.beans.factory.annotation.Value("${WEBHOOK_SECRET}")
    private String webhookSecret;

    @Transactional
    public String createCheckoutSession(String purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId).orElseThrow(() -> new EntityNotFoundException(("nie znaleziono tranzakji: " + purchaseId)));
        Stripe.apiKey = apiKey;

        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("Purchase " + purchaseId)
                .build();

        BigDecimal amount =  cartService.getPriceByUserId(purchase.getUser().getId());
        long unitAmount = amount.multiply(BigDecimal.valueOf(100)).longValue();

        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("pln")
                .setUnitAmount(unitAmount)
                .setProductData(productData)
                .build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(priceData)
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.BLIK)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.PAYPAL)
                .addLineItem(lineItem)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .putMetadata("purchaseId", purchaseId)
                .setSuccessUrl("http://localhost:2138/api/payments/success")
                .setCancelUrl("http://localhost:2138/api/payments/cancel")
                .build();

        try {
            com.stripe.model.checkout.Session session = com.stripe.model.checkout.Session.create(params);
            Payment payment = Payment.builder()
                    .id(UUID.randomUUID().toString())
                    .amount(unitAmount)
                    .createdAt(LocalDateTime.now())
                    .purchase(purchase)
                    .stripeSessionId(session.getId())
                    .status(Status.PENDING)
                    .build();
            paymentRepository.save(payment);
            return session.getUrl();
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void handleWebhook(String payload, String signature) {
        Stripe.apiKey = apiKey;
        Event event;
        try {
            event = Webhook.constructEvent(payload, signature, webhookSecret);
        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }

        if ("checkout.session.completed".equals(event.getType())) {
            StripeObject stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow();
            String sessionId = ((Session)stripeObject).getId();

            if (sessionId != null) {
                paymentRepository.findByStripeSessionId(sessionId).ifPresent(payment -> {
                    payment.setStatus(Status.COMPLETED);
                    payment.setPaidAt(LocalDateTime.now());
                    paymentRepository.save(payment);
                    Purchase purchase = payment.getPurchase();
                    purchaseService.order(purchase.getUser().getId(), purchase.getCart().getId());
                });
            }
        }
    }

}
