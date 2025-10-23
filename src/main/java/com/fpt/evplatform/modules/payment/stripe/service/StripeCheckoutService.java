package com.fpt.evplatform.modules.payment.stripe.service;

import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.membership.entity.MembershipPlan;
import com.fpt.evplatform.modules.membership.repository.MembershipPlanRepository;
import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.user.repository.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StripeCheckoutService {

    private final UserRepository userRepo;
    private final MembershipPlanRepository planRepo;

    @Value("${stripe.success-url}")
    private String successUrl;

    @Value("${stripe.cancel-url}")
    private String cancelUrl;

    public Map<String, Object> createCheckoutSession(String username, Integer planId) throws StripeException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        MembershipPlan plan = planRepo.findById(planId)
                .orElseThrow(() -> new AppException(ErrorCode.PLAN_NOT_FOUND));


        long amount = plan.getPrice().longValueExact(); // VND zero-decimal

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl) // phải chứa {CHECKOUT_SESSION_ID}
                .setCancelUrl(cancelUrl)
                // Line item theo giá hiện tại của plan
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("vnd")
                                                .setUnitAmount(amount)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(plan.getName())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                // Nhúng metadata vào PaymentIntent để verify khi activate
                .setPaymentIntentData(
                        SessionCreateParams.PaymentIntentData.builder()
                                .putMetadata("userId", String.valueOf(user.getUserId()))
                                .putMetadata("planId", String.valueOf(plan.getPlanId()))
                                .build()
                )
                .build();

        RequestOptions opts = RequestOptions.builder()
                .setIdempotencyKey("checkout_" + user.getUserId() + "_" + plan.getPlanId() + "_" + UUID.randomUUID())
                .build();

        Session session = Session.create(params, opts);

        return Map.of(
                "checkoutUrl", session.getUrl(),
                "sessionId", session.getId()
        );
    }
}
