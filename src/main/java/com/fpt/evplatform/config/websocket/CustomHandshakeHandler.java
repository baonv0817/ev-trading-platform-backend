package com.fpt.evplatform.config.websocket;

import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Map;

@Component
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        Object principal = attributes.get("principal");
        if (principal instanceof Principal userPrincipal) {
            return userPrincipal;
        }

        Object username = attributes.get("username");
        if (username instanceof String name) {
            return () -> name;
        }

        return null;
    }
}
