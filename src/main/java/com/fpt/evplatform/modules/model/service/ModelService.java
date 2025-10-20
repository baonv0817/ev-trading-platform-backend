package com.fpt.evplatform.modules.model.service;

import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.brand.entity.Brand;
import com.fpt.evplatform.modules.brand.repository.BrandRepository;
import com.fpt.evplatform.modules.model.dto.request.ModelRequest;
import com.fpt.evplatform.modules.model.dto.response.ModelResponse;
import com.fpt.evplatform.modules.model.entity.Model;
import com.fpt.evplatform.modules.model.mapper.ModelMapper;
import com.fpt.evplatform.modules.model.repository.ModelRepository;
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
public class ModelService {
    ModelRepository modelRepository;
    BrandRepository brandRepository;
    ModelMapper modelMapper;

    public List<ModelResponse> getAllModels() {
        return modelRepository.findAll()
                .stream()
                .map(modelMapper::toModelResponse)
                .toList();
    }

    public ModelResponse getModelById(Integer id) {
        Model model = modelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_NOT_FOUND));
        return modelMapper.toModelResponse(model);
    }

    public List<ModelResponse> getModelsByBrand(Integer brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
        return modelRepository.findByBrand(brand)
                .stream()
                .map(modelMapper::toModelResponse)
                .toList();
    }

    public ModelResponse createModel(ModelRequest req) {
        if (modelRepository.findByNameIgnoreCase(req.getName()).isPresent()) {
            throw new AppException(ErrorCode.MODEL_ALREADY_EXISTED);
        }

        Brand brand = brandRepository.findById(req.getBrandId())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));

        Model entity = modelMapper.toModel(req, brand);

        return modelMapper.toModelResponse(modelRepository.save(entity));
    }

    public ModelResponse updateModel(Integer id, ModelRequest req) {
        Model existing = modelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_NOT_FOUND));

        existing.setName(req.getName());

        Brand brand = brandRepository.findById(req.getBrandId())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
        existing.setBrand(brand);

        return modelMapper.toModelResponse(modelRepository.save(existing));
    }

    public void deleteModel(Integer id) {
        if (!modelRepository.existsById(id)) {
            throw new AppException(ErrorCode.MODEL_NOT_FOUND);
        }
        modelRepository.deleteById(id);
    }
}
