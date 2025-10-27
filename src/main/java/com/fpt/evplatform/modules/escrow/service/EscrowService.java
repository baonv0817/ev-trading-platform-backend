package com.fpt.evplatform.modules.escrow.service;

import com.fpt.evplatform.common.enums.EscrowStatus;
import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.deal.entity.Deal;
import com.fpt.evplatform.modules.escrow.dto.response.EscrowResponse;
import com.fpt.evplatform.modules.escrow.entity.Escrow;
import com.fpt.evplatform.modules.escrow.mapper.EscrowMapper;
import com.fpt.evplatform.modules.escrow.repository.EscrowRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EscrowService {

    EscrowRepository escrowRepository;
    EscrowMapper escrowMapper;

    private static final BigDecimal DEFAULT_RATE = BigDecimal.valueOf(5.0); // 5% fee


    @Transactional
    public EscrowResponse createEscrowForDeal(Deal deal) {
        if (escrowRepository.findByDeal(deal).isPresent()) {
            throw new AppException(ErrorCode.ESCROW_ALREADY_EXISTS);
        }

        BigDecimal fee = deal.getBalanceDue()
                .multiply(DEFAULT_RATE)
                .divide(BigDecimal.valueOf(100));

        Escrow escrow = Escrow.builder()
                .deal(deal)
                .ratePercent(DEFAULT_RATE)
                .feeAmount(fee)
                .holdStatus(EscrowStatus.HELD)
                .createdAt(LocalDateTime.now())
                .build();

        escrowRepository.save(escrow);
        log.info("Escrow created for deal ID {} (fee = {})", deal.getDealId(), fee);
        return escrowMapper.toResponse(escrow);
    }


    @Transactional
    public EscrowResponse releaseEscrow(Integer dealId) {
        Escrow escrow = escrowRepository.findByDeal_DealId(dealId)
                .orElseThrow(() -> new AppException(ErrorCode.ESCROW_NOT_FOUND));

        if (escrow.getHoldStatus() == EscrowStatus.RELEASED) {
            throw new AppException(ErrorCode.ESCROW_ALREADY_RELEASED);
        }

        escrow.setHoldStatus(EscrowStatus.RELEASED);
        escrow.setReleasedAt(LocalDateTime.now());
        escrowRepository.save(escrow);

        log.info("Escrow released for deal ID {}", dealId);
        return escrowMapper.toResponse(escrow);
    }

    @Transactional
    public void cancelEscrow(Integer dealId) {
        escrowRepository.findByDeal_DealId(dealId).ifPresent(escrow -> {
            escrow.setHoldStatus(EscrowStatus.CANCELLED);
            escrowRepository.save(escrow);
        });
    }

    public EscrowResponse getEscrowByDealId(Integer dealId) {
        Escrow escrow = escrowRepository.findByDeal_DealId(dealId)
                .orElseThrow(() -> new AppException(ErrorCode.ESCROW_NOT_FOUND));
        return escrowMapper.toResponse(escrow);
    }
}
