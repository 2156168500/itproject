package com.fjh.config;

import com.fjh.controller.GameController;
import com.fjh.controller.MatchController;
import com.fjh.controller.TestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private TestController testAPI;

    @Autowired
    private MatchController matchAPI;

    @Autowired
    private GameController gameAPI;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(testAPI, "/test");
        webSocketHandlerRegistry.addHandler(matchAPI, "/findMatch")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
        webSocketHandlerRegistry.addHandler(gameAPI, "/game")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }
}
