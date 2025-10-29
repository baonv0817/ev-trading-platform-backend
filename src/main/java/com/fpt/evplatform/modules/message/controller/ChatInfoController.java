package com.fpt.evplatform.modules.message.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(
        name = "Chat (WebSocket)",
        description = """
    This module provides **real-time chat functionality** between buyers and sellers
    using **WebSocket (STOMP)** with **JWT authentication** and **SockJS fallback**.

    ---
    ### 🔐 Authentication
    - The WebSocket connection must include a valid JWT token.
    - Example (local):
      `ws://localhost:8080/ws-chat?token={JWT}`
    - Example (production HTTPS):
      `wss://api.evplatform.com/ws-chat?token={JWT}`

    ---
    ### 💬 Chat Flow
    1. **Connect:** via `/ws-chat`
    2. **Send Message:** to `/app/chat.send`
    3. **Receive Message:** from `/user/queue/messages`

    ---
    ### 📦 Example JSON Payload
    **Send Message**
    ```json
    {
      "senderId": 5,
      "receiverId": 8,
      "content": "Hi, is the EV still available?",
      "conversationKey": "5_8"
    }
    ```

    **Receive Message**
    ```json
    {
      "senderId": 5,
      "receiverId": 8,
      "content": "Hi, is the EV still available?",
      "conversationKey": "5_8",
      "sentAt": "2025-10-29T14:12:32"
    }
    ```

    ---
    ### 🧭 STOMP Topics
    | Direction | Destination | Description |
    |------------|--------------|--------------|
    | `Send` | `/app/chat.send` | Send message to server |
    | `Receive` | `/user/queue/messages` | Receive private messages |

    ---
    ### ⚙️ Technical Notes
    - Protocol: STOMP over WebSocket
    - Authentication: JWT token in query parameter
    - Fallback: SockJS supported
    """
)
@SecurityRequirement(name = "bearerAuth")
public class ChatInfoController {

    @Operation(
            summary = "Get WebSocket chat connection info",
            description = """
            This endpoint provides information for frontend developers
            to correctly connect to the WebSocket chat system.
            """,
            parameters = {
                    @Parameter(
                            name = "token",
                            description = "JWT token required to authenticate the WebSocket session",
                            in = ParameterIn.QUERY,
                            required = true
                    )
            }
    )
    @GetMapping("/api/chat/info")
    public ApiResponse<Map<String, String>> getChatInfo() {
        return ApiResponse.<Map<String, String>>builder()
                .message("WebSocket Chat Configuration")
                .result(Map.of(
                        "endpoint", "ws://localhost:8080/ws-chat?token={JWT}",
                        "sendTo", "/app/chat.send",
                        "receiveFrom", "/user/queue/messages",
                        "protocol", "STOMP over WebSocket",
                        "auth", "JWT required in URL query parameter"
                ))
                .build();
    }
}
