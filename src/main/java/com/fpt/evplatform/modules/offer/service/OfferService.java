package com.fpt.evplatform.modules.offer.service;

import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.enums.OfferStatus;
import com.fpt.evplatform.common.exception.AppException;
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

    public OfferResponse createOffer(OfferRequest req) {
        User buyer = userRepository.findById(req.getBuyerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        SalePost listing = salePostRepository.findById(req.getListingId())
                .orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_FOUND));

        offerRepository.findByBuyerAndListing(buyer, listing)
                .ifPresent(o -> { throw new AppException(ErrorCode.OFFER_ALREADY_EXISTS); });

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

    public OfferResponse updateOfferStatus(Integer offerId, String status) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new AppException(ErrorCode.OFFER_NOT_FOUND));
        try {
            OfferStatus newStatus = OfferStatus.valueOf(status.toUpperCase());
            offer.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }
        offerRepository.save(offer);
        return offerMapper.toResponse(offer);
    }

    public void deleteOffer(Integer offerId) {
        if (!offerRepository.existsById(offerId)) {
            throw new AppException(ErrorCode.OFFER_NOT_FOUND);
        }
        offerRepository.deleteById(offerId);
    }

}
