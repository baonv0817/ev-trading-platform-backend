package com.fpt.evplatform.modules.vehiclepost.service;

import com.fpt.evplatform.modules.vehiclepost.entity.VehiclePost;
import com.fpt.evplatform.modules.vehiclepost.repository.VehiclePostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VehiclePostService {
    VehiclePostRepository vehiclePostRepository;
    
}
