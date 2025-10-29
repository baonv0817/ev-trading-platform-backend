package com.fpt.evplatform.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    PLAN_NOT_FOUND(2001, "Membership plan not found", HttpStatus.NOT_FOUND),
    PLAN_EXISTED(2002, "Membership plan existed", HttpStatus.BAD_REQUEST),
    PAYMENT_FAILED(3001, "Payment failed", HttpStatus.PAYMENT_REQUIRED),
    PAYMENT_METADATA_MISSING(3002, "Payment metadata is missing", HttpStatus.BAD_REQUEST),
    PAYMENT_FORBIDDEN(3003, "Payment is forbidden", HttpStatus.FORBIDDEN),
    SALE_POST_MEMBERSHIP_REQUIRED(4001, "You must have an active membership plan to create a sale post", HttpStatus.FORBIDDEN),
    SALE_POST_PLAN_EXPIRED(4002, "Your membership plan has expired", HttpStatus.FORBIDDEN),
    SALE_POST_LIMIT_REACHED(4003, "You have reached the maximum number of sale posts allowed for your plan", HttpStatus.BAD_REQUEST),
    BATTERY_DETAIL_REQUIRED(4101, "Battery detail is required for BATTERY product type", HttpStatus.BAD_REQUEST),
    VEHICLE_DETAIL_REQUIRED(4102, "Vehicle detail is required for VEHICLE product type", HttpStatus.BAD_REQUEST),
    BATTERY_DETAIL_MUST_BE_NULL(4103, "Battery detail must be null for VEHICLE product type", HttpStatus.BAD_REQUEST),
    VEHICLE_DETAIL_MUST_BE_NULL(4104, "Vehicle detail must be null for BATTERY product type", HttpStatus.BAD_REQUEST),
    BRAND_ALREADY_EXISTED(5001, "Brand is already existed", HttpStatus.BAD_REQUEST),
    BRAND_NOT_FOUND(5002, "Brand not found", HttpStatus.NOT_FOUND),
    MODEL_NOT_FOUND(5003, "Model not found", HttpStatus.NOT_FOUND),
    MODEL_ALREADY_EXISTED(5004, "Model already existed", HttpStatus.BAD_REQUEST),
    LISTING_NOT_FOUND(5005, "Listing not found", HttpStatus.NOT_FOUND),
    FAVORITE_NOT_FOUND(5006, "Favorite not found", HttpStatus.NOT_FOUND),
    FAVORITE_ALREADY_EXISTS(5007, "Favorite already exists", HttpStatus.BAD_REQUEST),
    REPORT_ALREADY_EXISTS(5008, "Report already exists", HttpStatus.BAD_REQUEST),
    BAD_REQUEST(7000,"Test", HttpStatus.BAD_REQUEST),
    REPORT_NOT_FOUND(5009, "Report not found", HttpStatus.NOT_FOUND),
    OFFER_ALREADY_EXISTS(6001, "Offer already exists", HttpStatus.BAD_REQUEST ),
    OFFER_NOT_FOUND(6002, "Offer not found", HttpStatus.NOT_FOUND),
    INVALID_STATUS(6003, "Status invalid", HttpStatus.BAD_REQUEST),
    PLATFORM_SITE_NOT_FOUND(6004, "Platform site not found" , HttpStatus.NOT_FOUND),
    DEAL_ALREADY_EXISTS(6005, "Deal already exists", HttpStatus.BAD_REQUEST),
    DEAL_NOT_FOUND(6006, "Deal not found", HttpStatus.NOT_FOUND),
    ESCROW_NOT_FOUND(6007, "Escrow not found", HttpStatus.NOT_FOUND),
    ESCROW_ALREADY_RELEASED(6008, "Escrow is already released", HttpStatus.BAD_REQUEST),
    ESCROW_ALREADY_EXISTS(6009, "Escrow already exists", HttpStatus.BAD_REQUEST),
    REVIEW_ALREADY_EXISTED(6010, "Review already exists", HttpStatus.BAD_REQUEST ),
    REVIEW_NOT_FOUND(6011, "Review not found", HttpStatus.NOT_FOUND),
    SENDER_NOT_FOUND(6012, "Sender not found", HttpStatus.NOT_FOUND),
    RECEIVER_NOT_FOUND(6013, "Receiver not found", HttpStatus.NOT_FOUND),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
