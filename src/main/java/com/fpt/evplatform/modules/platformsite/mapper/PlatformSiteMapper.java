package com.fpt.evplatform.modules.platformsite.mapper;

import com.fpt.evplatform.modules.platformsite.dto.request.PlatformSiteRequest;
import com.fpt.evplatform.modules.platformsite.dto.response.PlatformSiteResponse;
import com.fpt.evplatform.modules.platformsite.entity.PlatformSite;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface PlatformSiteMapper {

    PlatformSite toEntity(PlatformSiteRequest req);

    PlatformSiteResponse toResponse(PlatformSite entity);

    void updateEntityFromRequest(PlatformSiteRequest req, @MappingTarget PlatformSite entity);
}
