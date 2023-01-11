package com.example.assignment1.websockets;


import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
public class WebsocketEventListener {

    @EventListener
    public void handleWebsocketConnectListener(SessionConnectedEvent event) {
        System.out.println("Connected");
    }

}
