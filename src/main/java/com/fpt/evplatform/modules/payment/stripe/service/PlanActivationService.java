package com.fpt.evplatform.modules.payment.stripe.service;

import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.membership.dto.request.ActivatePlanRequest;
import com.fpt.evplatform.modules.membership.service.MembershipPlanService;
import com.fpt.evplatform.modules.user.dto.response.UserPlanResponse;
import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.user.repository.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlanActivationService {

    MembershipPlanService membershipPlanService;
    UserRepository userRepo;

    public UserPlanResponse activateAfterPayment(String username, String paymentIntentId) throws StripeException {
        PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);

        User currentUser = userRepo.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!"succeeded".equalsIgnoreCase(intent.getStatus())) {
            throw new AppException(ErrorCode.PAYMENT_FAILED);
        }

        String mdUserId = intent.getMetadata().get("userId");
        String mdPlanId = intent.getMetadata().get("planId");
        if (mdUserId == null || mdPlanId == null) {
            throw new AppException(ErrorCode.PAYMENT_METADATA_MISSING);
        }

        // Chặn tự ý kích hoạt của user khác
        if (!mdUserId.equals(String.valueOf(currentUser.getUserId()))) {
            throw new AppException(ErrorCode.PAYMENT_FORBIDDEN);
        }

        Integer planId = Integer.valueOf(mdPlanId);
        return membershipPlanService.activatePlan(currentUser.getUsername(), new ActivatePlanRequest(planId));
    }
}
