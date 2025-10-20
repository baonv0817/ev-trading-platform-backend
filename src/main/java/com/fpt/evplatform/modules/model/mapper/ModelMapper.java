package com.fpt.evplatform.modules.model.mapper;

import com.fpt.evplatform.modules.brand.entity.Brand;
import com.fpt.evplatform.modules.brand.mapper.BrandMapper;
import com.fpt.evplatform.modules.model.dto.request.ModelRequest;
import com.fpt.evplatform.modules.model.dto.response.ModelResponse;
import com.fpt.evplatform.modules.model.entity.Model;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {BrandMapper.class})
public interface ModelMapper {

    @Mapping(target = "modelId", ignore = true)
    @Mapping(target = "brand", source = "brand")
    @Mapping(target = "name", source = "req.name")
    Model toModel(ModelRequest req, Brand brand);

    @Mapping(target = "brandId", source = "brand.brandId")
    @Mapping(target = "brandName", source = "brand.name")
    ModelResponse toModelResponse(Model model);

    void updateModel(ModelRequest dto, @MappingTarget Model model);
}
