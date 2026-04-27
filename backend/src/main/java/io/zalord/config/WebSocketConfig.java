package io.zalord.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Frontend will use this URL to connect
        registry
            .addEndpoint("/ws")
            .setAllowedOrigins("http://localhost:3000")
            .withSockJS(); // Add fallback support for browsers that drop raw WebSockets
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // The prefix for topics that clients can subscribe to (e.g., /topic/chats/123)
        registry.enableSimpleBroker("/topic");

        // The prefix for messages sent FROM the client TO the server
        registry.setApplicationDestinationPrefixes("/app");
    }
}