package com.fpt.evplatform.modules.user.mapper;

import com.fpt.evplatform.modules.user.dto.response.UserPlanResponse;
import com.fpt.evplatform.modules.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserPlanMapper {
    @Mappings({
            @Mapping(target = "planId", source = "plan.planId"),
            @Mapping(target = "planName", source = "plan.name"),
            @Mapping(target = "price", source = "plan.price"),
            @Mapping(target = "status", source = "planStatus"),
            @Mapping(target = "startAt", source = "startAt"),
            @Mapping(target = "endAt", source = "endAt")
    })
    UserPlanResponse toUserPlanResponse(User user);
}
