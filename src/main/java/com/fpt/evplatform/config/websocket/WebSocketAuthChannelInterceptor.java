package com.fpt.evplatform.config.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object jwtAttr = accessor.getSessionAttributes() != null
                    ? accessor.getSessionAttributes().get("jwt")
                    : null;

            if (jwtAttr instanceof Jwt jwt) {
                String username = jwt.getSubject();

                List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                        new SimpleGrantedAuthority("USER")
                );

                Authentication auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities
                );

                accessor.setUser(auth);
                log.info("✅ STOMP CONNECT authenticated as: {}", username);
            } else {
                log.warn("⚠️ No JWT found in WebSocket session attributes");
            }
        }

        return message;
    }
}
