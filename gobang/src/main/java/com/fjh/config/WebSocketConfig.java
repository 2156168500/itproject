package com.fjh.config;

import com.fjh.API.TestAPI;
import com.fjh.controller.GameController;
import com.fjh.controller.MatchController;
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
    private TestAPI api;
    @Autowired
    private MatchController matchController;
    @Autowired
    private GameController gameController;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler( api,"/test")
                .addHandler(matchController,"/findMatch").addInterceptors(new HttpSessionHandshakeInterceptor())
                .addHandler(gameController,"/game").addInterceptors(new HttpSessionHandshakeInterceptor());
    }
}
