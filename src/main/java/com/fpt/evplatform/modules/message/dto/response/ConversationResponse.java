package com.fpt.evplatform.modules.message.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationResponse {
    Integer partnerId;
    String partnerName;
    String lastMessage;
    String lastMessageSenderName;
    String sentAt;
}
