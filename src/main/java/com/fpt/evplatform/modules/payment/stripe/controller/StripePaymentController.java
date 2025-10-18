package com.fpt.evplatform.modules.payment.stripe.controller;


import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.payment.stripe.service.PlanActivationService;
import com.fpt.evplatform.modules.payment.stripe.service.StripePaymentService;
import com.fpt.evplatform.modules.user.dto.response.UserPlanResponse;
import com.fpt.evplatform.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users/me/plan/payment")
@RequiredArgsConstructor
public class StripePaymentController {

    private final StripePaymentService stripePaymentService;
    private final PlanActivationService planActivationService;

    @PostMapping
    public ApiResponse<Map<String, Object>> createPayment(
            @AuthenticationPrincipal(expression = "subject") String username,
            @RequestBody Map<String, Integer> body) throws Exception {
        Integer planId = body.get("planId");
        var result = stripePaymentService.createPaymentIntent(username, planId);
        return ApiResponse.<Map<String, Object>>builder()
                .result(result)
                .message("Created PaymentIntent")
                .build();
    }

    @PostMapping("/activate")
    public ApiResponse<UserPlanResponse> activateAfterPayment(
            @AuthenticationPrincipal(expression = "subject") String username,
            @RequestBody Map<String, String> body) throws Exception {
        String paymentIntentId = body.get("paymentIntentId");
        var result = planActivationService.activateAfterPayment(username, paymentIntentId);
        return ApiResponse.<UserPlanResponse>builder()
                .result(result)
                .message("Plan activated")
                .build();
    }
}
