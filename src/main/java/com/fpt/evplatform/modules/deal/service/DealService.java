package com.fpt.evplatform.modules.deal.service;

import com.fpt.evplatform.common.enums.*;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.deal.dto.request.DealRequest;
import com.fpt.evplatform.modules.deal.dto.response.CreateCheckoutResponse;
import com.fpt.evplatform.modules.deal.dto.response.DealResponse;
import com.fpt.evplatform.modules.deal.entity.Deal;
import com.fpt.evplatform.modules.deal.mapper.DealMapper;
import com.fpt.evplatform.modules.deal.repository.DealRepository;
import com.fpt.evplatform.modules.escrow.entity.Escrow;
import com.fpt.evplatform.modules.escrow.repository.EscrowRepository;
import com.fpt.evplatform.modules.escrow.service.EscrowService;
import com.fpt.evplatform.modules.inspectionorder.config.InspectionConstants;
import com.fpt.evplatform.modules.offer.entity.Offer;
import com.fpt.evplatform.modules.offer.repository.OfferRepository;
import com.fpt.evplatform.modules.platformsite.entity.PlatformSite;
import com.fpt.evplatform.modules.platformsite.repository.PlatformSiteRepository;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.salepost.repository.SalePostRepository;
import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.user.repository.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class DealService {

    private final DealRepository dealRepository;
    private final DealMapper dealMapper;
    private final OfferRepository offerRepository;
    private final PlatformSiteRepository platformSiteRepository;
    private final EscrowRepository escrowRepository;
    private final EscrowService escrowService;
    private final UserRepository userRepository;
    private final SalePostRepository salePostRepository;

    @Value("${app.deal.success-url}") private String successUrl;
    @Value("${app.deal.cancel-url}")  private String cancelUrl;


    @Transactional
    public DealResponse createDeal(DealRequest req) {
        Offer offer = offerRepository.findById(req.getOfferId())
                .orElseThrow(() -> new AppException(ErrorCode.OFFER_NOT_FOUND));

        if (offer.getStatus() != OfferStatus.ACCEPTED) {
            throw new AppException(ErrorCode.OFFER_NOT_ACCEPTED);
        }

        if (dealRepository.findByOffer(offer).isPresent()) {
            throw new AppException(ErrorCode.DEAL_ALREADY_EXISTS);
        }

        boolean hasClosedDeal = dealRepository.existsByOffer_Listing_ListingIdAndStatusIn(
                offer.getListing().getListingId(),
                List.of(DealStatus.SCHEDULED, DealStatus.COMPLETED)
        );

        if (hasClosedDeal) {
            throw new AppException(ErrorCode.LISTING_UNAVAILABLE);
        }

        Deal deal = Deal.builder()
                .offer(offer)
                .status(DealStatus.INITIALIZED)
                .balanceDue(offer.getProposedPrice())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        dealRepository.save(deal);
        escrowService.createEscrowForDeal(deal);

        return dealMapper.toResponse(deal);
    }

    @Transactional
    public DealResponse assignPlatformSite(Integer dealId, DealRequest req) {
        if (req.getPlatformSiteId() == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        if (req.getScheduledAt() == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new AppException(ErrorCode.DEAL_NOT_FOUND));

        PlatformSite site = platformSiteRepository.findById(req.getPlatformSiteId())
                .orElseThrow(() -> new AppException(ErrorCode.PLATFORM_SITE_NOT_FOUND));

        LocalDateTime scheduledAt;
        try {
            scheduledAt = OffsetDateTime.parse(req.getScheduledAt()).toLocalDateTime();
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_DATE_FORMAT);
        }

        deal.setPlatformSite(site);
        deal.setScheduledAt(scheduledAt);
        deal.setStatus(DealStatus.AWAITING_CONFIRMATION);
        deal.setUpdatedAt(LocalDateTime.now());

        dealRepository.save(deal);
        return dealMapper.toResponse(deal);
    }

    @Transactional
    public DealResponse completeDeal(Integer dealId) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new AppException(ErrorCode.DEAL_NOT_FOUND));

        deal.setStatus(DealStatus.COMPLETED);
        deal.setUpdatedAt(LocalDateTime.now());
        dealRepository.save(deal);

        SalePost listing = deal.getOffer().getListing();
        listing.setStatus(PostStatus.SOLD);
        salePostRepository.save(listing);

        escrowService.releaseEscrow(deal.getDealId());

        return dealMapper.toResponse(deal);
    }

    public List<DealResponse> getAllDeals() {
        return dealRepository.findAll().stream()
                .map(dealMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<DealResponse> getDealsByStatus(DealStatus status) {
        return dealRepository.findByStatus(status).stream()
                .map(dealMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public DealResponse updateStatus(Integer dealId, DealStatus status) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new AppException(ErrorCode.DEAL_NOT_FOUND));

        deal.setStatus(status);
        deal.setUpdatedAt(LocalDateTime.now());
        dealRepository.save(deal);

        switch (status) {
            case CANCELLED, BUYER_NO_SHOW, SELLER_NO_SHOW -> {
                escrowService.cancelEscrow(deal.getDealId());
                SalePost listing = deal.getOffer().getListing();
                listing.setStatus(PostStatus.ACTIVE);
                salePostRepository.save(listing);
            }
            case COMPLETED -> {
                escrowService.releaseEscrow(deal.getDealId());
                SalePost listing = deal.getOffer().getListing();
                listing.setStatus(PostStatus.SOLD);
                salePostRepository.save(listing);
            }
            default -> {}
        }

        return dealMapper.toResponse(deal);
    }


    public void deleteDeal(Integer id) {
        if (!dealRepository.existsById(id)) {
            throw new AppException(ErrorCode.DEAL_NOT_FOUND);
        }
        dealRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<DealResponse> getDealsByBuyer(Integer buyerId) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return dealRepository.findByBuyer(buyer).stream()
                .map(dealMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DealResponse> getDealsBySeller(Integer sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return dealRepository.findBySeller(seller).stream()
                .map(dealMapper::toResponse)
                .collect(Collectors.toList());
    }

    public CreateCheckoutResponse createCheckout(Integer dealId) throws StripeException {
        Escrow escrow = getEscrowByDealId(dealId);
        long amount = escrow.getFeeAmount().longValueExact();
        String currency = InspectionConstants.DEFAULT_CURRENCY.toLowerCase(Locale.ROOT);

        SessionCreateParams params = com.stripe.param.checkout.SessionCreateParams.builder()
                .setMode(com.stripe.param.checkout.SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
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
                                                                .setName("Deal Order #" + dealId)
                                                                .setDescription("Deal ID: " + dealId)
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
    public void confirmPayment(Integer dealId) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new IllegalArgumentException("Deal not found"));

        if (deal.getStatus() != DealStatus.AWAITING_CONFIRMATION) {
            throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
        }

        deal.setStatus(DealStatus.SCHEDULED);
        deal.setUpdatedAt(LocalDateTime.now());
        dealRepository.save(deal);
    }

    public Escrow getEscrowByDealId(Integer dealId) {
        return escrowRepository.findByDeal_DealId(dealId)
                .orElseThrow(() -> new AppException(ErrorCode.ESCROW_NOT_FOUND));
    }

    @Transactional
    public void rejectPayment(Integer dealId) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new AppException(ErrorCode.DEAL_NOT_FOUND));

        if (deal.getStatus() != DealStatus.AWAITING_CONFIRMATION && deal.getStatus() != DealStatus.INITIALIZED) {
            throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
        }

        deal.setStatus(DealStatus.CANCELLED);
        dealRepository.save(deal);

        SalePost listing = deal.getOffer().getListing();
        listing.setStatus(PostStatus.ACTIVE);
        salePostRepository.save(listing);
    }



}
