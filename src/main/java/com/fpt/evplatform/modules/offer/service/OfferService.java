package com.fpt.evplatform.modules.offer.service;

import com.fpt.evplatform.common.enums.DealStatus;
import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.enums.OfferStatus;
import com.fpt.evplatform.common.enums.PostStatus;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.deal.dto.request.DealRequest;
import com.fpt.evplatform.modules.deal.dto.response.DealResponse;
import com.fpt.evplatform.modules.deal.repository.DealRepository;
import com.fpt.evplatform.modules.deal.service.DealService;
import com.fpt.evplatform.modules.offer.dto.request.OfferRequest;
import com.fpt.evplatform.modules.offer.dto.response.OfferResponse;
import com.fpt.evplatform.modules.offer.entity.Offer;
import com.fpt.evplatform.modules.offer.mapper.OfferMapper;
import com.fpt.evplatform.modules.offer.repository.OfferRepository;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.salepost.repository.SalePostRepository;
import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OfferService {

    OfferRepository offerRepository;
    UserRepository userRepository;
    SalePostRepository salePostRepository;
    OfferMapper offerMapper;
    DealService dealService;
    DealRepository dealRepository;

    public OfferResponse createOffer(OfferRequest req) {
        User buyer = userRepository.findById(req.getBuyerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        SalePost listing = salePostRepository.findById(req.getListingId())
                .orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_FOUND));

        boolean hasClosedDeal = dealRepository.existsByOffer_Listing_ListingIdAndStatusIn(
                req.getListingId(),
                List.of(DealStatus.SCHEDULED, DealStatus.COMPLETED)
        );
        if (hasClosedDeal) {
            throw new AppException(ErrorCode.LISTING_UNAVAILABLE);
        }

        offerRepository.findByBuyerAndListing(buyer, listing)
                .ifPresent(o -> {
                    if (o.getStatus() != OfferStatus.REJECTED) {
                        throw new AppException(ErrorCode.OFFER_ALREADY_EXISTS);
                    }
                });

        Offer offer = offerMapper.toEntity(req, buyer, listing);
        offerRepository.save(offer);
        return offerMapper.toResponse(offer);
    }

    public List<OfferResponse> getOffersByBuyer(Integer buyerId) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return offerRepository.findByBuyer(buyer).stream()
                .map(offerMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<OfferResponse> getOffersByListing(Integer listingId) {
        SalePost listing = salePostRepository.findById(listingId)
                .orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_FOUND));
        return offerRepository.findByListing(listing).stream()
                .map(offerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OfferResponse updateOfferStatus(Integer offerId, String status) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new AppException(ErrorCode.OFFER_NOT_FOUND));

        OfferStatus newStatus;
        try {
            newStatus = OfferStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        if (newStatus == OfferStatus.ACCEPTED) {
            boolean alreadyAccepted = offerRepository.findByListing(offer.getListing()).stream()
                    .anyMatch(o -> o.getStatus() == OfferStatus.ACCEPTED);
            if (alreadyAccepted) {
                throw new AppException(ErrorCode.DEAL_ALREADY_EXISTS);
            }

            offerRepository.findByListing(offer.getListing()).forEach(o -> {
                if (!o.getOfferId().equals(offer.getOfferId()) && o.getStatus() == OfferStatus.PENDING) {
                    o.setStatus(OfferStatus.REJECTED);
                    offerRepository.save(o);
                }
            });

            offer.setStatus(OfferStatus.ACCEPTED);
            offerRepository.save(offer);

            SalePost listing = offer.getListing();
            listing.setStatus(PostStatus.HIDDEN);
            salePostRepository.save(listing);

            DealRequest dealReq = new DealRequest();
            dealReq.setOfferId(offerId);
            DealResponse dealResponse = dealService.createDeal(dealReq);
            log.info("✅ Deal created for accepted offer ID {} -> deal ID {}", offerId, dealResponse.getDealId());
        } else {
            offer.setStatus(newStatus);
            offerRepository.save(offer);
        }

        return offerMapper.toResponse(offer);
    }

    public void deleteOffer(Integer offerId) {
        if (!offerRepository.existsById(offerId)) {
            throw new AppException(ErrorCode.OFFER_NOT_FOUND);
        }
        offerRepository.deleteById(offerId);
    }

    public List<OfferResponse> getOffersBySeller(Integer sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<SalePost> sellerListings = salePostRepository.findBySeller(seller);
        return offerRepository.findByListingIn(sellerListings)
                .stream()
                .map(offerMapper::toResponse)
                .collect(Collectors.toList());
    }
}

