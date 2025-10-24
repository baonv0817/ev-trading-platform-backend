package com.fpt.evplatform.modules.platformsite.service;

import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.platformsite.dto.request.PlatformSiteRequest;
import com.fpt.evplatform.modules.platformsite.dto.response.PlatformSiteResponse;
import com.fpt.evplatform.modules.platformsite.entity.PlatformSite;
import com.fpt.evplatform.modules.platformsite.mapper.PlatformSiteMapper;
import com.fpt.evplatform.modules.platformsite.repository.PlatformSiteRepository;
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
public class PlatformSiteService {

    PlatformSiteRepository platformSiteRepository;
    PlatformSiteMapper platformSiteMapper;

    public List<PlatformSiteResponse> getAllSites() {
        return platformSiteRepository.findAll()
                .stream()
                .map(platformSiteMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<PlatformSiteResponse> getActiveSites() {
        return platformSiteRepository.findAllByActiveTrue()
                .stream()
                .map(platformSiteMapper::toResponse)
                .collect(Collectors.toList());
    }

    public PlatformSiteResponse getSiteById(Integer id) {
        PlatformSite site = platformSiteRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PLATFORM_SITE_NOT_FOUND));
        return platformSiteMapper.toResponse(site);
    }

    public PlatformSiteResponse createSite(PlatformSiteRequest req) {
        PlatformSite site = platformSiteMapper.toEntity(req);
        return platformSiteMapper.toResponse(platformSiteRepository.save(site));
    }

    public PlatformSiteResponse updateSite(Integer id, PlatformSiteRequest req) {
        PlatformSite existing = platformSiteRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PLATFORM_SITE_NOT_FOUND));
        platformSiteMapper.updateEntityFromRequest(req, existing);
        return platformSiteMapper.toResponse(platformSiteRepository.save(existing));
    }

    public void deleteSite(Integer id) {
        if (!platformSiteRepository.existsById(id)) {
            throw new AppException(ErrorCode.PLATFORM_SITE_NOT_FOUND);
        }
        platformSiteRepository.deleteById(id);
    }
}
