package com.fpt.evplatform.config.websocket;

import com.fpt.evplatform.config.CustomJwtDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private final CustomJwtDecoder jwtDecoder;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    Jwt jwt = jwtDecoder.decode(token);

                    if (accessor.getSessionAttributes() != null) {
                        accessor.getSessionAttributes().put("jwt", jwt);
                    } else {
                        log.warn("Session attributes null, cannot store JWT");
                    }

                    Authentication auth = new UsernamePasswordAuthenticationToken(jwt.getSubject(), null, null);
                    accessor.setUser(auth);

                    log.info("✅ WebSocket CONNECT authenticated for user: {}", jwt.getSubject());
                } catch (Exception e) {
                    log.warn("❌ Invalid JWT in WebSocket CONNECT: {}", e.getMessage());
                }
            } else {
                log.warn("⚠️ No Authorization header in STOMP CONNECT");
            }
        }

        return message;
    }
}
