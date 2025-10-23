package com.fpt.evplatform.modules.offer.mapper;

import com.fpt.evplatform.modules.offer.dto.response.OfferResponse;
import com.fpt.evplatform.modules.offer.entity.Offer;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.user.entity.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.fpt.evplatform.modules.offer.dto.request.OfferRequest;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface OfferMapper {
    @Mapping(target = "offerId", ignore = true)
    @Mapping(target = "buyer", source = "buyer")
    @Mapping(target = "listing", source = "listing")
    @Mapping(target = "proposedPrice", source = "req.proposedPrice")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "createdAt", ignore = true)
    Offer toEntity(OfferRequest req, User buyer, SalePost listing);

    @Mapping(target = "buyerId", source = "buyer.userId")
    @Mapping(target = "buyerName", expression = "java(offer.getBuyer().getUsername())")
    @Mapping(target = "listingId", source = "listing.listingId")
    OfferResponse toResponse(Offer offer);
}
