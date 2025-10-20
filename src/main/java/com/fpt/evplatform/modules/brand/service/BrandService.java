package com.fpt.evplatform.modules.brand.service;

import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.brand.dto.request.BrandRequest;
import com.fpt.evplatform.modules.brand.dto.response.BrandResponse;
import com.fpt.evplatform.modules.brand.entity.Brand;
import com.fpt.evplatform.modules.brand.mapper.BrandMapper;
import com.fpt.evplatform.modules.brand.repository.BrandRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandService {
    BrandRepository brandRepository;
    BrandMapper brandMapper;

    public List<BrandResponse> getAllBrands() {
        return brandRepository.findAll()
                .stream()
                .map(brandMapper::toBrandResponse)
                .toList();
    }

    public BrandResponse getBrandById(Integer id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
        return brandMapper.toBrandResponse(brand);
    }

    public BrandResponse createBrand(BrandRequest request) {
        if (brandRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new AppException(ErrorCode.BRAND_ALREADY_EXISTED);
        }

        Brand brand = brandMapper.toBrand(request);
        return brandMapper.toBrandResponse(brandRepository.save(brand));
    }

    public BrandResponse updateBrand(Integer id, BrandRequest request) {
        Brand existingBrand = brandRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));

        existingBrand.setName(request.getName());
        return brandMapper.toBrandResponse(brandRepository.save(existingBrand));
    }

    public void deleteBrandById(Integer id) {
        if (!brandRepository.existsById(id)) {
            throw new AppException(ErrorCode.BRAND_NOT_FOUND);
        }
        brandRepository.deleteById(id);
    }
}
