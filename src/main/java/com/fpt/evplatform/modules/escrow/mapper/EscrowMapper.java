package com.fpt.evplatform.modules.escrow.mapper;

import com.fpt.evplatform.modules.escrow.dto.response.EscrowResponse;
import com.fpt.evplatform.modules.escrow.entity.Escrow;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface EscrowMapper {

    @Mapping(target = "dealId", source = "deal.dealId")
    EscrowResponse toResponse(Escrow escrow);

}
