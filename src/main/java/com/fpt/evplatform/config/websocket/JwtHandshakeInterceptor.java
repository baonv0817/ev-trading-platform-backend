package com.fpt.evplatform.config.websocket;

import com.fpt.evplatform.config.CustomJwtDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final CustomJwtDecoder jwtDecoder;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        URI uri = request.getURI();
        String path = uri.getPath();
        String query = uri.getQuery();

        log.info("🌐 Incoming WebSocket handshake request:");
        log.info("   • Path: {}", path);
        log.info("   • Query: {}", query);

        // ✅ Only skip internal SockJS handshake probes (NOT the real WebSocket)
        if (path.contains("/info") || path.contains("/xhr")) {
            log.debug("ℹ️ Allowing internal SockJS handshake path: {}", path);
            return true;
        }

        String token = getTokenFromQuery(query);
        if (token == null || token.isBlank()) {
            log.warn("⚠️ No JWT token found in WebSocket request to {}", path);
            return false;
        }

        try {
            Jwt jwt = jwtDecoder.decode(token);
            String username = jwt.getSubject();

            attributes.put("jwt", jwt);
            attributes.put("user", username);

            log.info("✅ WebSocket handshake authenticated for user: {}", username);
            return true;
        } catch (Exception e) {
            log.error("❌ Invalid JWT in WebSocket handshake: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            log.error("💥 Exception during WebSocket handshake: {}", exception.getMessage());
        } else {
            log.debug("🤝 Handshake completed successfully for {}", request.getURI());
        }
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
}
