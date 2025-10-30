package com.fpt.evplatform.modules.message.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {
    Integer senderId;
    Integer receiverId;
    String content;
    String conversationKey;
    String sentAt;
}
