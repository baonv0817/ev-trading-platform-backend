package com.fpt.evplatform.modules.membership.mapper;

import com.fpt.evplatform.modules.membership.entity.MembershipPlan;
import com.fpt.evplatform.modules.membership.dto.request.MembershipPlanCreationRequest;
import com.fpt.evplatform.modules.membership.dto.request.MembershipPlanUpdateRequest;
import com.fpt.evplatform.modules.membership.dto.response.MembershipPlanResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MembershipPlanMapper {
    MembershipPlan toMembershipPlan(MembershipPlanCreationRequest request);
    MembershipPlanResponse toMembershipPlanResponse(MembershipPlan membershipPlan);
    void updateMembershipPlan(@MappingTarget MembershipPlan membershipPlan, MembershipPlanUpdateRequest request);
}
