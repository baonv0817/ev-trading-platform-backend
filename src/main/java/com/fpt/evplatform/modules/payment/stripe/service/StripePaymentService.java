package com.fpt.evplatform.modules.payment.stripe.service;


import com.fpt.evplatform.modules.membership.entity.MembershipPlan;
import com.fpt.evplatform.modules.membership.repository.MembershipPlanRepository;
import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.user.repository.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StripePaymentService {

    UserRepository userRepo;
    MembershipPlanRepository planRepo;

    public Map<String, Object> createPaymentIntent(String username, Integer planId) throws StripeException {
        User user = userRepo.findByUsername(username).orElseThrow();
        MembershipPlan plan = planRepo.findById(planId).orElseThrow();

        long amount = plan.getPrice().longValueExact();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency("VND")
                .setDescription("Purchase membership plan: " + plan.getName())
                .putMetadata("userId", String.valueOf(user.getUserId()))
                .putMetadata("planId", String.valueOf(plan.getPlanId()))
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .setAllowRedirects(
                                        PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER
                                )
                                .build()
                )
                .build();

        // Idempotency để tránh tạo trùng khi FE retry
        RequestOptions opts = RequestOptions.builder()
                .setIdempotencyKey("pi_" + user.getUserId() + "_" + plan.getPlanId() + "_" + UUID.randomUUID())
                .build();

        PaymentIntent intent = PaymentIntent.create(params, opts);
        System.out.println(intent.getMetadata());
        return Map.of(
                "clientSecret", intent.getClientSecret(),
                "paymentIntentId", intent.getId()
        );
    }
}
