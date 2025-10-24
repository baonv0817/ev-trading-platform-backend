package com.fpt.evplatform.modules.deal.service;

import com.fpt.evplatform.common.enums.DealStatus;
import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.deal.dto.request.DealRequest;
import com.fpt.evplatform.modules.deal.dto.response.DealResponse;
import com.fpt.evplatform.modules.deal.entity.Deal;
import com.fpt.evplatform.modules.deal.mapper.DealMapper;
import com.fpt.evplatform.modules.deal.repository.DealRepository;
import com.fpt.evplatform.modules.offer.entity.Offer;
import com.fpt.evplatform.modules.offer.repository.OfferRepository;
import com.fpt.evplatform.modules.platformsite.entity.PlatformSite;
import com.fpt.evplatform.modules.platformsite.repository.PlatformSiteRepository;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DealService {

    DealRepository dealRepository;
    DealMapper dealMapper;
    OfferRepository offerRepository;
    PlatformSiteRepository platformSiteRepository;

    public DealResponse createDeal(DealRequest req) {
        Offer offer = offerRepository.findById(req.getOfferId())
                .orElseThrow(() -> new AppException(ErrorCode.OFFER_NOT_FOUND));
        PlatformSite site = platformSiteRepository.findById(req.getPlatformSiteId())
                .orElseThrow(() -> new AppException(ErrorCode.PLATFORM_SITE_NOT_FOUND));

        if (dealRepository.findByOffer(offer).isPresent()) {
            throw new AppException(ErrorCode.DEAL_ALREADY_EXISTS);
        }

        Deal deal = dealMapper.toEntity(req, offer, site);
        dealRepository.save(deal);
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

    public DealResponse updateStatus(Integer dealId, DealStatus status) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new AppException(ErrorCode.DEAL_NOT_FOUND));
        deal.setStatus(status);
        dealRepository.save(deal);
        return dealMapper.toResponse(deal);
    }

    public void deleteDeal(Integer id) {
        if (!dealRepository.existsById(id)) {
            throw new AppException(ErrorCode.DEAL_NOT_FOUND);
        }
        dealRepository.deleteById(id);
    }
}
