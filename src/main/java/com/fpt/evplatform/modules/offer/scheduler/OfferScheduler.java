package com.fpt.evplatform.modules.offer.scheduler;

import com.fpt.evplatform.common.enums.OfferStatus;
import com.fpt.evplatform.modules.offer.entity.Offer;
import com.fpt.evplatform.modules.offer.repository.OfferRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OfferScheduler {

    OfferRepository offerRepository;

    // Run every hour
    @Scheduled(cron = "0 0 * * * *")
    public void expireOldOffers() {
        List<Offer> expiredOffers = offerRepository.findByStatusAndExpiresAtBefore(OfferStatus.PENDING, LocalDateTime.now());

        if (expiredOffers.isEmpty()) return;

        expiredOffers.forEach(o -> o.setStatus(OfferStatus.EXPIRED));
        offerRepository.saveAll(expiredOffers);

        log.info("Expired {} offers automatically.", expiredOffers.size());
    }
}
