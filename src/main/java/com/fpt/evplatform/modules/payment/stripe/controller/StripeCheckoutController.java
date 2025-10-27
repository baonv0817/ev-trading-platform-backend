package com.fpt.evplatform.modules.payment.stripe.controller;


import com.fpt.evplatform.common.dto.ApiResponse;

import com.fpt.evplatform.modules.payment.stripe.dto.ActivateFromSessionRequest;
import com.fpt.evplatform.modules.payment.stripe.dto.CreateCheckoutRequest;
import com.fpt.evplatform.modules.payment.stripe.service.PlanActivationFromSessionService;
import com.fpt.evplatform.modules.payment.stripe.service.StripeCheckoutService;
import com.fpt.evplatform.modules.user.dto.response.UserPlanResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users/me/plan/checkout")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class StripeCheckoutController {

    private final StripeCheckoutService checkoutService;
    private final PlanActivationFromSessionService activationService;

    @PostMapping
    public ApiResponse<Map<String, Object>> createCheckout(
            @AuthenticationPrincipal(expression = "subject") String username,
            @Valid @RequestBody CreateCheckoutRequest req) throws Exception {

        var result = checkoutService.createCheckoutSession(username, req.getPlanName());
        return ApiResponse.<Map<String, Object>>builder()
                .result(result)
                .message("Checkout Session created")
                .build();
    }

    @PostMapping("/activate-from-session")
    public ApiResponse<UserPlanResponse> activateFromSession(
            @Valid @RequestBody ActivateFromSessionRequest req) throws Exception {
        var result = activationService.activate(req.getSessionId());
        return ApiResponse.<UserPlanResponse>builder()
                .result(result)
                .message("Plan activated from Checkout Session")
                .build();
    }
}
