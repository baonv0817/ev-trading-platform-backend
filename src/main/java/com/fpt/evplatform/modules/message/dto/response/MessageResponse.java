package com.fpt.evplatform.modules.message.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponse {
    Integer messageId;
    Integer senderId;
    String senderName;
    Integer receiverId;
    String receiverName;
    String body;
    LocalDateTime sentAt;
    String conversationKey;
}
