package com.fpt.evplatform.modules.message.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class MessageRequest {
    Integer senderId;
    Integer receiverId;
    String body;
}
