package com.fpt.evplatform.modules.inspectionorder.controller;

import com.fpt.evplatform.modules.inspectionorder.dto.request.CreateOrderRequest;
import com.fpt.evplatform.modules.inspectionorder.dto.request.FinishInspectionRequest;
import com.fpt.evplatform.modules.inspectionorder.dto.response.CreateCheckoutResponse;
import com.fpt.evplatform.modules.inspectionorder.service.InspectionOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inspection-orders")
@RequiredArgsConstructor
public class InspectionOrderController {

    private final InspectionOrderService orderService;

    // 1. Tạo order
    @PostMapping
    public Integer createOrder(@RequestBody CreateOrderRequest req) {
        return orderService.createOrder(req);
    }

    // 2. Lấy URL thanh toán Stripe
    @PostMapping("/{orderId}/checkout")
    public ResponseEntity<CreateCheckoutResponse> createCheckout(@PathVariable Integer orderId) throws Exception {
        return ResponseEntity.ok(orderService.createCheckout(orderId));
    }

    // 3. FE gọi khi user thanh toán xong (Stripe success redirect)
    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<Void> confirmPayment(@PathVariable Integer orderId) {
        orderService.confirmPayment(orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/inspect")
    public void completeInspection(@PathVariable Integer orderId, @RequestBody FinishInspectionRequest req) {
        orderService.completeInspection(orderId, req);
    }
}
