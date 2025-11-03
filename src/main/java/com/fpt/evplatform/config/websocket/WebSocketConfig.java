package com.fpt.evplatform.config.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.config.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("🚀 Registering STOMP endpoint at /ws-chat");

        registry.addEndpoint("/ws-chat")
                .addInterceptors(jwtHandshakeInterceptor)
                .setAllowedOriginPatterns("http://localhost:5173")
                .withSockJS();

        log.info("✅ SockJS endpoint /ws-chat registered (allowed origin: http://localhost:5173)");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        log.info("⚙️ Configuring message broker:");
        log.info("   • Application prefix: /app");
        log.info("   • Broker prefixes: /topic, /queue");
        log.info("   • User prefix: /user");

        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

                if (accessor != null && accessor.getUser() == null) {
                    var sessionAttributes = accessor.getSessionAttributes();
                    if (sessionAttributes != null) {
                        Object username = sessionAttributes.get("user");
                        if (username != null) {
                            Principal principal = new UsernamePasswordAuthenticationToken(
                                    username, null, List.of()
                            );
                            accessor.setUser(principal);
                            log.info("👤 Principal set for WebSocket session: {}", username);
                        }
                    }
                }

                return message;
            }
        });
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        log.info("📡 WebSocket transport configured (sendTime=60s, buffer=50MB, message=50MB)");
        registry.setSendTimeLimit(60 * 1000)
                .setSendBufferSizeLimit(50 * 1024 * 1024)
                .setMessageSizeLimit(50 * 1024 * 1024);
    }
}
