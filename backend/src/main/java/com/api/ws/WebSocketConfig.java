package com.api.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final SimulationSocket socket;
    private final SessionIdInterceptor interceptor;

    public WebSocketConfig(SimulationSocket socket,
                           SessionIdInterceptor interceptor) {
        this.socket = socket;
        this.interceptor = interceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socket, "/ws/sim/{sessionId}")
                .addInterceptors(interceptor)
                .setAllowedOrigins("*");
    }
}