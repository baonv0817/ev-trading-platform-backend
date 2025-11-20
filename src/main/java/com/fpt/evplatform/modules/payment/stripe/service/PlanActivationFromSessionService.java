package com.fpt.evplatform.modules.payment.stripe.service;

import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.membership.dto.request.ActivatePlanRequest;
import com.fpt.evplatform.modules.membership.entity.MembershipPlan;
import com.fpt.evplatform.modules.membership.repository.MembershipPlanRepository;
import com.fpt.evplatform.modules.membership.service.MembershipPlanService;
import com.fpt.evplatform.modules.payment.transaction.service.TransactionService;
import com.fpt.evplatform.modules.user.dto.response.UserPlanResponse;
import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.user.repository.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanActivationFromSessionService {

    private final MembershipPlanService membershipPlanService;
    private final UserRepository userRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final TransactionService transactionService;

    public UserPlanResponse activate(String sessionId) throws StripeException {
        Session session = Session.retrieve(sessionId);

        // Yêu cầu Checkout đã hoàn tất & đã thanh toán
        if (!"complete".equalsIgnoreCase(session.getStatus())) {
            throw new AppException(ErrorCode.BAD_REQUEST);
        }
        if (!"paid".equalsIgnoreCase(session.getPaymentStatus())) {
            throw new AppException(ErrorCode.BAD_REQUEST);
        }

        // Lấy PaymentIntent từ session để verify thêm và kích hoạt
        String piId = session.getPaymentIntent();
        if (piId == null) {
            throw new AppException(ErrorCode.BAD_REQUEST);
        }
        PaymentIntent pi = PaymentIntent.retrieve(piId);

        if (!"succeeded".equalsIgnoreCase(pi.getStatus())) {
            throw new AppException(ErrorCode.BAD_REQUEST);
        }

        String mdUserId = pi.getMetadata().get("userId");
        Integer userId = Integer.parseInt(mdUserId);
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String mdPlanId = pi.getMetadata().get("planId");

        Integer planId = Integer.valueOf(mdPlanId);
        MembershipPlan plan = membershipPlanRepository.findById(planId).orElseThrow(() -> new AppException(ErrorCode.PLAN_NOT_FOUND));
        transactionService.recordPayment(
                user.getUserId(),
                plan.getPlanId(),
                "MEMBERSHIP",
                plan.getPrice().longValueExact()
        );

        // (tùy chọn) verify currentUsername ↔ mdUserId để chống kích hoạt chéo tài khoản
        // Integer currentUserId = currentUser.idFromUsername(currentUsername);
        // if (!String.valueOf(currentUserId).equals(mdUserId)) throw new AppException(ErrorCode.FORBIDDEN, "...");

        // Kích hoạt theo planId rút ra từ metadata
        return membershipPlanService.activatePlan(user.getUsername(), new ActivatePlanRequest(planId));
    }
}
