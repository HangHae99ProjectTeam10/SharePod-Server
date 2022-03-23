package com.spring.sharepod.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //메시지 구독하는 요청은 /sub
        config.enableSimpleBroker("/sub");
        //preifx 시작하는 /pub
        config.setApplicationDestinationPrefixes("/pub");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //endpoint, 개발 서버 접속 주소 : 토큰을 가지고 http://localhost:8080/ws-stomp로 접속하면
        // Handshake 일어나면서 HTTP->WS 프로토콜 변결. "Welcome to SockJS!"가 출력됨
        registry.addEndpoint("/ws-stomp").setAllowedOriginPatterns("*")
                .withSockJS(); // sock.js를 통하여 낮은 버전의 브라우저에서도 websocket이 동작할수 있게
    }
}
