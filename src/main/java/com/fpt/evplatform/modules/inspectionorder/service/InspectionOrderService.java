package com.fpt.evplatform.modules.inspectionorder.service;

import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.enums.InspectionOrderStatus;
import com.fpt.evplatform.common.enums.InspectionReportResult;
import com.fpt.evplatform.common.enums.PaymentStatus;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.inspectionorder.config.InspectionConstants;
import com.fpt.evplatform.modules.inspectionorder.dto.request.CreateOrderRequest;
import com.fpt.evplatform.modules.inspectionorder.dto.request.FinishInspectionRequest;
import com.fpt.evplatform.modules.inspectionorder.dto.response.CreateCheckoutResponse;
import com.fpt.evplatform.modules.inspectionorder.entity.InspectionOrder;
import com.fpt.evplatform.modules.inspectionorder.repository.InspectionOrderRepository;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.salepost.repository.SalePostRepository;
import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.user.repository.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class InspectionOrderService {

    private final InspectionOrderRepository orderRepo;
    private final SalePostRepository salePostRepo;
    private final InspectionReportFlowService reportFlowService;

    @Value("${app.checkout.success-url}") private String successUrl;
    @Value("${app.checkout.cancel-url}")  private String cancelUrl;

    // 1. Tạo order (2.000.000 VND)
    @Transactional
    public Integer createOrder(CreateOrderRequest req) {
        SalePost sp = salePostRepo.findById(req.getListingId())
                .orElseThrow(() -> new IllegalArgumentException("SalePost not found"));

        InspectionOrder order = InspectionOrder.builder()
                .salePost(sp)
                .scheduledAt(req.getScheduledAt())
                .provinceCode(req.getProvinceCode())
                .districtCode(req.getDistrictCode())
                .wardCode(req.getWardCode())
                .street(req.getStreet())
                .status(InspectionOrderStatus.PENDING)
                .paymentStatus(PaymentStatus.UNPAID)
                .amount(InspectionConstants.INSPECTION_FEE_VND)
                .createdAt(LocalDateTime.now())
                .build();

        return orderRepo.save(order).getOrderId();
    }

    // 2. Tạo phiên Stripe Checkout (không webhook)
    public CreateCheckoutResponse createCheckout( Integer orderId) throws StripeException {
        InspectionOrder order = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (order.getPaymentStatus() == PaymentStatus.PAID)
            throw new IllegalStateException("Order đã thanh toán rồi");

        long amount = InspectionConstants.INSPECTION_FEE_VND.longValueExact(); // 2,000,000
        String currency = InspectionConstants.DEFAULT_CURRENCY.toLowerCase(Locale.ROOT);

        SessionCreateParams params = com.stripe.param.checkout.SessionCreateParams.builder()
                .setMode(com.stripe.param.checkout.SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl) // phải chứa {CHECKOUT_SESSION_ID}
                .setCancelUrl(cancelUrl)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(currency)
                                                .setUnitAmount(amount)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Inspection Order #" + orderId)
                                                                .setDescription("Listing ID: " + order.getSalePost().getListingId())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        Session session = Session.create(params);
        return new CreateCheckoutResponse(session.getId(), session.getUrl());
    }

    // 3. FE gọi sau khi thanh toán thành công (Stripe redirect về)
    @Transactional
    public void confirmPayment(Integer orderId) {
        InspectionOrder order = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (order.getPaymentStatus() == PaymentStatus.PAID)
            throw new IllegalStateException("Order đã xác nhận thanh toán rồi");

        order.setPaymentStatus(PaymentStatus.PAID);
        order.setPaidAt(LocalDateTime.now());
        order.setStatus(InspectionOrderStatus.SCHEDULED); // Đã lên lịch kiểm định
        orderRepo.save(order);

        log.info("Order #{} payment confirmed manually by user", orderId);
    }

    // 4. Bắt đầu kiểm định (tuỳ chọn)
//    @Transactional
//    public void startInspection(Integer orderId) {
//        InspectionOrder order = orderRepo.findById(orderId)
//                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
//
//        if (order.getPaymentStatus() != PaymentStatus.PAID)
//            throw new IllegalStateException("Order chưa thanh toán!");
//
//        order.setStatus(InspectionOrderStatus.IN_PROGRESS);
//        orderRepo.save(order);
//    }

    // 5. Hoàn tất kiểm định
    @Transactional
    public void completeInspection(Integer orderId, FinishInspectionRequest req) {
        InspectionOrder order = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (order.getPaymentStatus() != PaymentStatus.PAID)
            throw new IllegalStateException("Order chưa thanh toán!");

        order.setStatus(InspectionOrderStatus.COMPLETED);
        orderRepo.save(order);

        reportFlowService.createReportFromInspectionResult(order, req.getResult());
    }
}