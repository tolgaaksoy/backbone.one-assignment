package one.backbone.messagingassignment.service;

import one.backbone.messagingassignment.model.dto.MessageDto;
import one.backbone.messagingassignment.model.dto.request.SendMessageRequest;

import java.util.List;

public interface MessageService {
    MessageDto sendMessage(SendMessageRequest request);

    List<MessageDto> retrieveMessagesByUserId(Long userId, String dateFrom, String dateTo);
}
