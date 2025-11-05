package com.fpt.evplatform.modules.salepost.mapper;

import com.fpt.evplatform.modules.batterypost.dto.response.BatteryPostResponse;
import com.fpt.evplatform.modules.batterypost.entity.BatteryPost;
import com.fpt.evplatform.modules.salepost.dto.response.PostResponse;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.vehiclepost.dto.response.VehiclePostResponse;
import com.fpt.evplatform.modules.vehiclepost.entity.VehiclePost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SalePostMapper {

    @Mapping(source = "seller.userId", target = "sellerId")
    @Mapping(source = "seller.username", target = "sellerUsername")
    @Mapping(source = "seller.phone", target = "sellerPhone")
    @Mapping(source = "seller.avatarUrl", target = "sellerAvatarUrl")
    PostResponse toPostResponse(SalePost salePost);

    BatteryPostResponse toBatteryPostResponse(BatteryPost batteryPost);

    @Mapping(source = "model.name", target = "modelName")
    @Mapping(source = "model.brand.name", target = "brandName")
    VehiclePostResponse toVehiclePostResponse(VehiclePost vehiclePost);
}
