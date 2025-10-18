package com.fpt.evplatform.modules.membership.service;

import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.membership.dto.request.ActivatePlanRequest;
import com.fpt.evplatform.modules.membership.entity.MembershipPlan;
import com.fpt.evplatform.modules.membership.repository.MembershipPlanRepository;
import com.fpt.evplatform.modules.membership.dto.request.MembershipPlanCreationRequest;
import com.fpt.evplatform.modules.membership.dto.request.MembershipPlanUpdateRequest;
import com.fpt.evplatform.modules.membership.dto.response.MembershipPlanResponse;
import com.fpt.evplatform.modules.membership.mapper.MembershipPlanMapper;
import com.fpt.evplatform.modules.user.dto.response.UserPlanResponse;
import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.user.mapper.UserPlanMapper;
import com.fpt.evplatform.modules.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MembershipPlanService {
    MembershipPlanRepository membershipPlanRepository;
    MembershipPlanMapper membershipPlanMapper;
    UserRepository userRepo;
    UserPlanMapper userPlanMapper;

    public MembershipPlanResponse createMembershipPlan(MembershipPlanCreationRequest request) {
        if (membershipPlanRepository.existsByName(request.getName()))
            throw new AppException(ErrorCode.PLAN_EXISTED);
        MembershipPlan plan = membershipPlanMapper.toMembershipPlan(request);
        return membershipPlanMapper.toMembershipPlanResponse(membershipPlanRepository.save(plan));
    }

    public MembershipPlanResponse updateMembershipPlan(Integer planId, MembershipPlanUpdateRequest request) {
        MembershipPlan plan = membershipPlanRepository.findById(planId)
                .orElseThrow(() -> new AppException(ErrorCode.PLAN_NOT_FOUND));
        membershipPlanMapper.updateMembershipPlan(plan, request);
        return membershipPlanMapper.toMembershipPlanResponse(membershipPlanRepository.save(plan));
    }

    public List<MembershipPlanResponse> getMembershipPlans() {
        return membershipPlanRepository.findAll().stream()
                .map(membershipPlanMapper::toMembershipPlanResponse).toList();
    }

    public void deletePlan(Integer planId) {
        membershipPlanRepository.deleteById(planId);
    }

//    public void activatePlan(String username, ActivatePlanRequest request) {
//        User user = userRepo.findByUsername(username)
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//
//        MembershipPlan plan = membershipPlanRepository.findById(request.getPlanId())
//                .orElseThrow(() -> new AppException(ErrorCode.PLAN_NOT_FOUND));
//
//        LocalDateTime startAt = LocalDateTime.now();
//        LocalDateTime endAt = startAt.plusDays(plan.getDurationDays());
//
//        user.setPlan(plan);
//        user.setPlanStatus("ACTIVE");
//        user.setStartAt(startAt);
//        user.setEndAt(endAt);
//        userRepo.save(user);
//    }

    public UserPlanResponse activatePlan(String username, ActivatePlanRequest request) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        MembershipPlan plan = membershipPlanRepository.findById(request.getPlanId())
                .orElseThrow(() -> new AppException(ErrorCode.PLAN_NOT_FOUND));

        Integer duration = plan.getDurationDays();

        // Nếu user còn hạn, nối tiếp thời gian
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime baseTime = now;
        if (user.getEndAt() != null && user.getEndAt().isAfter(now)) {
            baseTime = user.getEndAt();
        }

        LocalDateTime startAt = now;
        LocalDateTime endAt = baseTime.plusDays(duration);

        user.setPlan(plan);
        user.setPlanStatus("ACTIVE");
        user.setStartAt(startAt);
        user.setEndAt(endAt);

        userRepo.save(user);

        // 👉 Build response
        UserPlanResponse response = new UserPlanResponse();
        response.setPlanId(plan.getPlanId());
        response.setPlanName(plan.getName());
        response.setPrice(plan.getPrice());
        response.setStatus(user.getPlanStatus());
        response.setStartAt(user.getStartAt());
        response.setEndAt(user.getEndAt());

        return response;
    }


    public void cancelPlan(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPlanStatus("CANCELED");
        userRepo.save(user);
    }

    public UserPlanResponse getCurrentPlan(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userPlanMapper.toUserPlanResponse(user);
    }
}