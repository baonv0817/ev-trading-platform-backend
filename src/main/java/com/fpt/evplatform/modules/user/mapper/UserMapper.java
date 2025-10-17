package com.fpt.evplatform.modules.user.mapper;

import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.user.dto.request.UserCreationRequest;
import com.fpt.evplatform.modules.user.dto.request.UserUpdateRequest;
import com.fpt.evplatform.modules.user.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
