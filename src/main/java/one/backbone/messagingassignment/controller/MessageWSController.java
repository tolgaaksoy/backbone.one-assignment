package one.backbone.messagingassignment.controller;

import one.backbone.messagingassignment.model.dto.request.GetMessageRequest;
import one.backbone.messagingassignment.model.dto.request.SendMessageRequest;
import one.backbone.messagingassignment.model.entity.Message;
import one.backbone.messagingassignment.model.entity.User;
import one.backbone.messagingassignment.service.MessageWSService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class MessageWSController {

    private final MessageWSService messageWSService;

    public MessageWSController(MessageWSService messageWSService) {
        this.messageWSService = messageWSService;
    }

    @MessageMapping("/send")
    @SendToUser("/queue/messages")
    public Message sendMessage(Principal principal, SendMessageRequest messageRequest) {
        String messageText = messageRequest.getMessage();
        User sender = new User();
        sender.setId(messageRequest.getSenderId());
        User recipient = new User();
        recipient.setId(messageRequest.getRecipientId());
        Message newMessage = messageWSService.sendMessage(messageText, sender, recipient);

        // Broadcast the newMessage to the recipient user via WebSocket
        return newMessage;
    }

    @MessageMapping("/retrieve")
    @SendToUser("/queue/messages")
    public List<Message> getMessagesByUserId(Principal principal, GetMessageRequest messageRetrieveRequest) {
        Long userId = Long.parseLong(principal.getName());
        LocalDateTime fromDate = convertToDate(messageRetrieveRequest.getFrom());
        LocalDateTime toDate = convertToDate(messageRetrieveRequest.getTo());

        List<Message> messages;
        if (fromDate != null && toDate != null) {
            messages = messageWSService.getMessagesByUserIdAndDateRange(userId, fromDate, toDate);
        } else {
            messages = messageWSService.getMessagesByUserId(userId);
        }

        return messages;
    }

    private LocalDateTime convertToDate(String date) {
        LocalDateTime result = null;
        if (date != null) {
            result = LocalDateTime.parse(date);
        }
        return result;
    }
}
