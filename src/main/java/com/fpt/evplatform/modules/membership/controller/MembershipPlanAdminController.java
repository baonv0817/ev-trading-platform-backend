package com.fpt.evplatform.modules.membership.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.membership.service.MembershipPlanService;
import com.fpt.evplatform.modules.membership.dto.request.MembershipPlanCreationRequest;
import com.fpt.evplatform.modules.membership.dto.request.MembershipPlanUpdateRequest;
import com.fpt.evplatform.modules.membership.dto.response.MembershipPlanResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/plans")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MembershipPlanAdminController {
    MembershipPlanService membershipPlanService;

    @PostMapping
    ApiResponse<MembershipPlanResponse> createMembershipPlan(@RequestBody MembershipPlanCreationRequest membershipPlan) {
        return ApiResponse.<MembershipPlanResponse>builder()
                .result(membershipPlanService.createMembershipPlan(membershipPlan))
                .build();
    }

    @PutMapping("/{planId}")
    ApiResponse<MembershipPlanResponse> updateMembershipPlan(@PathVariable Integer planId,@RequestBody MembershipPlanUpdateRequest membershipPlan) {
        return ApiResponse.<MembershipPlanResponse>builder()
                .result(membershipPlanService.updateMembershipPlan(planId, membershipPlan))
                .build();
    }

    @GetMapping
    ApiResponse<List<MembershipPlanResponse>> getMembershipPlan() {
        return ApiResponse.<List<MembershipPlanResponse>>builder()
                .result(membershipPlanService.getMembershipPlans())
                .build();
    }

    @DeleteMapping("/{planId}")
    ApiResponse<Void> deleteMembershipPlan(@PathVariable Integer planId) {
        membershipPlanService.deletePlan(planId);
        return ApiResponse.<Void>builder()
                .message("Membership plan deleted successfully")
                .build();
    }


}
