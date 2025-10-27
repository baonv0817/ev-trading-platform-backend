package com.fpt.evplatform.modules.message.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.message.dto.request.MessageRequest;
import com.fpt.evplatform.modules.message.dto.response.MessageResponse;
import com.fpt.evplatform.modules.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Message", description = "Manage Messages between Users")
public class MessageController {

    MessageService messageService;

    @Operation(summary = "User send a Message")
    @PostMapping
    public ApiResponse<MessageResponse> sendMessage(@RequestBody MessageRequest request) {
        return ApiResponse.<MessageResponse>builder()
                .result(messageService.sendMessage(request))
                .message("Message sent successfully")
                .build();
    }

    @Operation(summary = "Get Chat history between 2 Users")
    @GetMapping("/conversation")
    public ApiResponse<List<MessageResponse>> getConversation(@RequestParam Integer user1Id, @RequestParam Integer user2Id) {
        return ApiResponse.<List<MessageResponse>>builder()
                .result(messageService.getConversation(user1Id, user2Id))
                .message("Conversation retrieved successfully")
                .build();
    }

    @Operation(summary = "Get Message history of a User")
    @GetMapping("/user/{userId}")
    public ApiResponse<List<MessageResponse>> getUserMessages(@PathVariable Integer userId) {
        return ApiResponse.<List<MessageResponse>>builder()
                .result(messageService.getMessagesOfUser(userId))
                .message("User messages retrieved successfully")
                .build();
    }
}
