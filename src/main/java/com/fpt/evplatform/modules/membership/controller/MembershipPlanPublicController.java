package com.fpt.evplatform.modules.membership.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.membership.dto.response.MembershipPlanResponse;
import com.fpt.evplatform.modules.membership.service.MembershipPlanService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MembershipPlanPublicController {
    MembershipPlanService membershipPlanService;

    @GetMapping
    ApiResponse<List<MembershipPlanResponse>> getMembershipPlan() {
        return ApiResponse.<List<MembershipPlanResponse>>builder()
                .result(membershipPlanService.getMembershipPlans())
                .build();
    }
}
