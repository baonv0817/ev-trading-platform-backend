package com.fpt.evplatform.modules.user.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.common.enums.Role;
import com.fpt.evplatform.modules.user.service.UserService;
import com.fpt.evplatform.modules.user.dto.request.UserCreationRequest;
import com.fpt.evplatform.modules.user.dto.request.UserUpdateRequest;
import com.fpt.evplatform.modules.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request, Role.USER.name()))
                .build();
    }



    @GetMapping
    ApiResponse<List<UserResponse>> getUsers(){
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }


    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") Integer userId){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable Integer userId){
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .result("User has been deleted")
                .build();
    }

    @PostMapping("/{userId}")
    ApiResponse<String> banUser(@PathVariable Integer userId){
        userService.banUser(userId);
        return ApiResponse.<String>builder()
                .result("User has been banned")
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable Integer userId, @RequestBody UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }
}
