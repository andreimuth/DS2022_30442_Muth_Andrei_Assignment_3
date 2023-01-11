package com.example.assignment1.websockets;

import com.example.assignment1.dto.ChatMessage;
import com.example.assignment1.dto.TypingNotification;
import com.example.assignment1.entities.User;
import com.example.assignment1.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebsocketController {
    private final UserService userService;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/ws/admin")
    @SendTo("/topic/message/admin")
    public ChatMessage sendMessageToAdmin(@Payload ChatMessage message) {
        message.setTo(userService.findAdmin().getUsername());
        message.setSeen(false);
        return message;
    }

    @MessageMapping("/ws/user")
    public void sendMessageToUser(@Payload ChatMessage message) {
        User user = userService.findByUsername(message.getTo());
        message.setSeen(false);
        messagingTemplate.convertAndSend("/topic/message/user/" + user.getId(), message);
    }

    @MessageMapping("/ws/admin/mark-messages")
    public void markMessagesFromAdminAsSeen(@Payload String username) {
        messagingTemplate.convertAndSend("/topic/messages-seen/admin", username);
    }

    @MessageMapping("/ws/user/mark-messages")
    public void markMessagesFromUserAsSeen(@Payload String username) {
        User user = userService.findByUsername(username);
        messagingTemplate.convertAndSend("/topic/messages-seen/user/" + user.getId(), true);
    }

    @MessageMapping("/ws/admin/typing")
    public void userTypingNotification(@Payload TypingNotification notification) {
        messagingTemplate.convertAndSend("/topic/typing/admin", notification);
    }

    @MessageMapping("/ws/user/typing")
    public void adminTypingNotification(@Payload TypingNotification notification) {
        System.out.println(notification.getToUsername());
        if(notification.getToUsername() != null) {
            User user = userService.findByUsername(notification.getToUsername());
            messagingTemplate.convertAndSend("/topic/typing/user/" + user.getId(), notification);
        }
    }

}
