package one.backbone.messagingassignment.service;

import one.backbone.messagingassignment.model.entity.Message;
import one.backbone.messagingassignment.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageWSService {
    Message sendMessage(String messageText, User sender, User recipient);

    List<Message> getMessagesByUserId(Long userId);

    List<Message> getMessagesByUserIdAndDateRange(Long userId, LocalDateTime dateFrom, LocalDateTime dateTo);

}
