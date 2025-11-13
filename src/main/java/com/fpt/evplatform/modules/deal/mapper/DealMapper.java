package com.fpt.evplatform.modules.deal.mapper;

import com.fpt.evplatform.common.enums.DealStatus;
import com.fpt.evplatform.modules.deal.dto.request.DealRequest;
import com.fpt.evplatform.modules.deal.dto.response.DealResponse;
import com.fpt.evplatform.modules.deal.entity.Deal;
import com.fpt.evplatform.modules.offer.entity.Offer;
import com.fpt.evplatform.modules.platformsite.entity.PlatformSite;
import com.fpt.evplatform.modules.user.dto.SimpleUserDTO;
import com.fpt.evplatform.modules.user.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DealMapper {

    @Mapping(target = "dealId", ignore = true)
    @Mapping(target = "offer", source = "offer")
    @Mapping(target = "platformSite", source = "platformSite")
    @Mapping(target = "balanceDue", source = "req.balanceDue")
    @Mapping(target = "status", expression = "java(com.fpt.evplatform.common.enums.DealStatus.INITIALIZED)")
    @Mapping(target = "scheduledAt",
            expression = "java(req.getScheduledAt() != null ? java.time.LocalDateTime.parse(req.getScheduledAt()) : null)")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Deal toEntity(DealRequest req, Offer offer, PlatformSite platformSite);

    @Mapping(target = "offerId", source = "offer.offerId")
    @Mapping(target = "platformSiteId", source = "platformSite.platformSiteId")
    @Mapping(target = "platformSiteName", source = "platformSite.name")
    @Mapping(target = "status", expression = "java(deal.getStatus().name())")
    @Mapping(target = "buyer", source = "offer.buyer")
    @Mapping(target = "seller", source = "offer.listing.seller")
    @Mapping(target = "listingId", source = "offer.listing.listingId")
    DealResponse toResponse(Deal deal);

    default SimpleUserDTO toSimpleUserDTO(User user) {
        if (user == null) return null;
        return SimpleUserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .fullName(user.getLastName() + " " + user.getFirstName())
                .phone(user.getPhone())
                .build();
    }
}
