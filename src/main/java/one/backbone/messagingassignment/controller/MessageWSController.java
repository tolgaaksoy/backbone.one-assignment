package one.backbone.messagingassignment.controller;

import one.backbone.messagingassignment.model.dto.MessageDto;
import one.backbone.messagingassignment.model.dto.request.GetMessageRequest;
import one.backbone.messagingassignment.model.dto.request.SendMessageRequest;
import one.backbone.messagingassignment.model.entity.User;
import one.backbone.messagingassignment.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
public class MessageWSController {

    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public MessageWSController(MessageService messageService,
                               SimpMessagingTemplate simpMessagingTemplate) {
        this.messageService = messageService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/send-message")
    @SendTo("/topic/messages")
    public MessageDto sendMessage(@Payload SendMessageRequest request) {
        // Process the request and send the message
        MessageDto message = messageService.sendMessage(request);

        // Broadcast the message to the sender and recipient
        simpMessagingTemplate.convertAndSendToUser(message.getSenderId().toString(), "/queue/messages", message);
        simpMessagingTemplate.convertAndSendToUser(message.getRecipientId().toString(), "/queue/messages", message);

        // Return the message object to be sent to the topic
        return message;
    }

    @SubscribeMapping("/queue/messages")
    public List<MessageDto> handleSubscription(@Payload(required = false) GetMessageRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();

            if (!Objects.equals(user.getId(), request.getRecipientId())) {
                // If the recipient id in the request does not match the authenticated user's id, return an empty list
                return Collections.emptyList();
            }

            // Retrieve previous messages for the authenticated user from the database
            return messageService.getMessages(request);
        }

        return Collections.emptyList();
    }
}
