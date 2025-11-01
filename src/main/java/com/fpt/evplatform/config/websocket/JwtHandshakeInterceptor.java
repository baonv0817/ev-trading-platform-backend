package com.fpt.evplatform.config.websocket;

import com.fpt.evplatform.config.CustomJwtDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor, ChannelInterceptor {

    private final CustomJwtDecoder jwtDecoder;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        URI uri = request.getURI();
        String path = uri.getPath();

        if (path.contains("/info") || path.contains("/xhr") || path.contains("/websocket")) {
            return true;
        }

        String token = getTokenFromQuery(uri.getQuery());
        if (token == null || token.isBlank()) {
            log.warn("No token found in WebSocket connection request to {}", path);
            return false;
        }

        try {
            Jwt jwt = jwtDecoder.decode(token);

            attributes.put("jwt", jwt);
            request.getAttributes();
            request.getAttributes().put("jwt", jwt);

            log.info("✅ WebSocket connected for user: {}", jwt.getSubject());
            return true;
        } catch (Exception e) {
            log.error("❌ Invalid JWT: {}", e.getMessage());
            return false;
        }
    }


    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }

    private String getTokenFromQuery(String query) {
        if (query == null) return null;
        for (String param : query.split("&")) {
            if (param.startsWith("token=")) {
                return param.substring(6);
            }
        }
        return null;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        return message;
    }
}
