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
import com.fpt.evplatform.modules.inspectionorder.dto.response.InspectionOrderResponse;
import com.fpt.evplatform.modules.inspectionorder.entity.InspectionOrder;
import com.fpt.evplatform.modules.inspectionorder.repository.InspectionOrderRepository;
import com.fpt.evplatform.modules.payment.transaction.service.TransactionService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final TransactionService transactionService;

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

        long amount = InspectionConstants.INSPECTION_FEE_VND.longValueExact();
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


        transactionService.recordPayment(
                order.getSalePost().getSeller().getUserId(),
                order.getOrderId(),
                "INSPECTION",
                InspectionConstants.INSPECTION_FEE_VND.longValueExact()
        );


        log.info("Order #{} payment confirmed manually by user", orderId);
    }

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


    public Page<InspectionOrderResponse> listAll(Pageable pageable) {
        return orderRepo.findAll(pageable).map(this::toResponse);
    }

    private InspectionOrderResponse toResponse(InspectionOrder od) {
        InspectionOrderResponse dto = new InspectionOrderResponse();
        dto.setOrderId(od.getOrderId());
        dto.setListingId(od.getSalePost().getListingId());
        dto.setStatus(od.getStatus());
        dto.setPaymentStatus(od.getPaymentStatus());
        dto.setScheduledAt(od.getScheduledAt());
        dto.setProvinceCode(od.getProvinceCode());
        dto.setDistrictCode(od.getDistrictCode());
        dto.setWardCode(od.getWardCode());
        dto.setStreet(od.getStreet());
        dto.setAmount(od.getAmount());
        dto.setCreatedAt(od.getCreatedAt());
        dto.setPaidAt(od.getPaidAt());
        return dto;
    }
}