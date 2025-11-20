package com.fpt.evplatform.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    // ==================================================
    // 🔹 COMMON ERRORS (1000–1999)
    // ==================================================
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid key", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(1002, "Invalid request", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1003, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1004, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1005, "You do not have permission", HttpStatus.FORBIDDEN),
    UNAUTHORIZED_ACTION(1006, "Unauthorized action", HttpStatus.UNAUTHORIZED),
    BAD_REQUEST(1007, "Bad request", HttpStatus.BAD_REQUEST),


    // ==================================================
    // 🔹 USER ERRORS (1100–1199)
    // ==================================================
    USER_EXISTED(1101, "User already existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1102, "User not existed", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1103, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1104, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),

    // ==================================================
    // 🔹 MEMBERSHIP / PLAN ERRORS (2000–2099)
    // ==================================================
    PLAN_NOT_FOUND(2001, "Membership plan not found", HttpStatus.NOT_FOUND),
    PLAN_EXISTED(2002, "Membership plan existed", HttpStatus.BAD_REQUEST),
    SALE_POST_MEMBERSHIP_REQUIRED(2003, "You must have an active membership plan to create a sale post", HttpStatus.FORBIDDEN),
    SALE_POST_PLAN_EXPIRED(2004, "Your membership plan has expired", HttpStatus.FORBIDDEN),
    SALE_POST_LIMIT_REACHED(2005, "You have reached the maximum number of sale posts allowed for your plan", HttpStatus.BAD_REQUEST),
    USER_HAD_MEMBERSHIP(2006, "You already had a membership plan", HttpStatus.BAD_REQUEST),
    // ==================================================
    // 🔹 PAYMENT / STRIPE ERRORS (3000–3099)
    // ==================================================
    PAYMENT_FAILED(3001, "Payment failed", HttpStatus.PAYMENT_REQUIRED),
    PAYMENT_METADATA_MISSING(3002, "Payment metadata is missing", HttpStatus.BAD_REQUEST),
    PAYMENT_FORBIDDEN(3003, "Payment is forbidden", HttpStatus.FORBIDDEN),

    // ==================================================
    // 🔹 SALE POST & PRODUCT DETAIL ERRORS (4000–4199)
    // ==================================================
    POST_ALREADY_SOLD(4001, "Post already sold", HttpStatus.BAD_REQUEST),
    ASK_PRICE_MUST_POSITIVE(4002, "Ask price must be positive", HttpStatus.BAD_REQUEST),
    BATTERY_DETAIL_REQUIRED(4101, "Battery detail is required for BATTERY product type", HttpStatus.BAD_REQUEST),
    VEHICLE_DETAIL_REQUIRED(4102, "Vehicle detail is required for VEHICLE product type", HttpStatus.BAD_REQUEST),
    BATTERY_DETAIL_MUST_BE_NULL(4103, "Battery detail must be null for VEHICLE product type", HttpStatus.BAD_REQUEST),
    VEHICLE_DETAIL_MUST_BE_NULL(4104, "Vehicle detail must be null for BATTERY product type", HttpStatus.BAD_REQUEST),
    VEHICLE_YEAR_REQUIRED(4105, "Vehicle year is required for VEHICLE year", HttpStatus.BAD_REQUEST),
    VEHICLE_YEAR_INVALID(4106, "Vehicle year is invalid", HttpStatus.BAD_REQUEST),
    VEHICLE_ODO_REQUIRED(4107, "Odo is required for VEHICLE ODO", HttpStatus.BAD_REQUEST),
    VEHICLE_ODO_INVALID(4018,"Vehicle ODO is invalid", HttpStatus.BAD_REQUEST),
    VEHICLE_SEAT_COUNT_INVALID(4109, "Vehicle seatcount is invalid", HttpStatus.BAD_REQUEST),
    VEHICLE_VIN_INVALID(4110,"Vehicle vin is invalid", HttpStatus.BAD_REQUEST),
    VEHICLE_VIN_DUPLICATE(4111,"Vehicle vin already exists", HttpStatus.BAD_REQUEST),
    SALE_POST_NOT_FOUND(4112,"Sale post not found", HttpStatus.NOT_FOUND),
    // ==================================================
    // 🔹 BRAND / MODEL / LISTING ERRORS (5000–5099)
    // ==================================================
    BRAND_ALREADY_EXISTED(5001, "Brand already existed", HttpStatus.BAD_REQUEST),
    BRAND_NOT_FOUND(5002, "Brand not found", HttpStatus.NOT_FOUND),
    MODEL_ALREADY_EXISTED(5003, "Model already existed", HttpStatus.BAD_REQUEST),
    MODEL_NOT_FOUND(5004, "Model not found", HttpStatus.NOT_FOUND),
    LISTING_NOT_FOUND(5005, "Listing not found", HttpStatus.NOT_FOUND),
    LISTING_UNAVAILABLE(5006, "Listing is unavailable (already has a deal)", HttpStatus.BAD_REQUEST),
    FAVORITE_ALREADY_EXISTS(5007, "Favorite already exists", HttpStatus.BAD_REQUEST),
    FAVORITE_NOT_FOUND(5008, "Favorite not found", HttpStatus.NOT_FOUND),
    REPORT_ALREADY_EXISTS(5009, "Report already exists", HttpStatus.BAD_REQUEST),
    REPORT_NOT_FOUND(5010, "Report not found", HttpStatus.NOT_FOUND),

    // ==================================================
    // 🔹 OFFER / DEAL / ESCROW ERRORS (6000–6099)
    // ==================================================
    OFFER_ALREADY_EXISTS(6001, "Offer already exists", HttpStatus.BAD_REQUEST),
    OFFER_NOT_FOUND(6002, "Offer not found", HttpStatus.NOT_FOUND),
    OFFER_NOT_ACCEPTED(6003, "Only accepted offers can create a deal", HttpStatus.BAD_REQUEST),
    INVALID_STATUS(6004, "Status invalid", HttpStatus.BAD_REQUEST),

    DEAL_ALREADY_EXISTS(6005, "Deal already exists", HttpStatus.BAD_REQUEST),
    DEAL_NOT_FOUND(6006, "Deal not found", HttpStatus.NOT_FOUND),
    DEAL_NOT_ELIGIBLE_FOR_REVIEW(6007, "Deal not eligible for review", HttpStatus.BAD_REQUEST),
    INVALID_STATUS_TRANSITION(6008, "Invalid deal status transition", HttpStatus.BAD_REQUEST),
    INVALID_DATE_FORMAT(6009, "Invalid date format", HttpStatus.BAD_REQUEST),

    PLATFORM_SITE_NOT_FOUND(6010, "Platform site not found", HttpStatus.NOT_FOUND),

    ESCROW_NOT_FOUND(6011, "Escrow not found", HttpStatus.NOT_FOUND),
    ESCROW_ALREADY_EXISTS(6012, "Escrow already exists", HttpStatus.BAD_REQUEST),
    ESCROW_ALREADY_RELEASED(6013, "Escrow already released", HttpStatus.BAD_REQUEST),

    // ==================================================
    // 🔹 CHAT / MESSAGE ERRORS (7000–7099)
    // ==================================================
    SENDER_NOT_FOUND(7001, "Sender not found", HttpStatus.NOT_FOUND),
    RECEIVER_NOT_FOUND(7002, "Receiver not found", HttpStatus.NOT_FOUND),

    // ==================================================
    // 🔹 REVIEW ERRORS (8000–8099)
    // ==================================================
    REVIEW_ALREADY_EXISTED(8001, "Review already existed", HttpStatus.BAD_REQUEST),
    REVIEW_NOT_FOUND(8002, "Review not found", HttpStatus.NOT_FOUND),

    // ==================================================
    // 🔹 FILE ERRORS (9000–9099)
    // ==================================================
    FILE_REQUIRED(9001, "File is required", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(9002, "File is too large", HttpStatus.BAD_REQUEST),
    FILE_TYPE_NOT_ALLOWED(9003, "File type not allowed", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(9004, "File upload failed", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
