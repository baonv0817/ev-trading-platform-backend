package com.fpt.evplatform.modules.membership.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.membership.dto.request.ActivatePlanRequest;
import com.fpt.evplatform.modules.membership.service.MembershipPlanService;
import com.fpt.evplatform.modules.user.dto.response.UserPlanResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/users/me/plan")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "bearerAuth")
public class MembershipPlanUserController {
    MembershipPlanService membershipPlanService;

    @GetMapping
    ApiResponse<UserPlanResponse> getMyPlan(@AuthenticationPrincipal(expression = "subject") String username) {
        return ApiResponse.<UserPlanResponse>builder()
                .result(membershipPlanService.getCurrentPlan(username))
                .build();
    }

    @PostMapping("/activate")
    public ApiResponse<Void> activate(@AuthenticationPrincipal(expression = "subject") String username,
                                      @RequestBody ActivatePlanRequest request) {
        membershipPlanService.activatePlan(username, request);
        return ApiResponse.<Void>builder()
                .message("Plan activated successfully")
                .build();
    }

    @PostMapping("/cancel")
    public ApiResponse<Void> cancel(@AuthenticationPrincipal(expression = "subject") String username) {
        membershipPlanService.cancelPlan(username);
        return ApiResponse.<Void>builder()
                .message("Plan cancelled successfully")
                .build();
    }
}
