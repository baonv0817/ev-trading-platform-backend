package com.fpt.evplatform.modules.message.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("body")
    String content;
    String conversationKey;
    String sentAt;
}
