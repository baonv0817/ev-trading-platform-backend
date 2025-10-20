package com.fpt.evplatform.modules.brand.mapper;

import com.fpt.evplatform.modules.brand.dto.request.BrandRequest;
import com.fpt.evplatform.modules.brand.dto.response.BrandResponse;
import com.fpt.evplatform.modules.brand.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    Brand toBrand(BrandRequest dto);

    BrandResponse toBrandResponse(Brand entity);

    void updateBrand(BrandRequest dto, @MappingTarget Brand brand);
}
