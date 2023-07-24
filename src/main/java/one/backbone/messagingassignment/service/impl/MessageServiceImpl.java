package one.backbone.messagingassignment.service.impl;

import lombok.extern.slf4j.Slf4j;
import one.backbone.messagingassignment.model.dto.MessageDto;
import one.backbone.messagingassignment.model.dto.request.GetMessageRequest;
import one.backbone.messagingassignment.model.dto.request.SendMessageRequest;
import one.backbone.messagingassignment.repository.MessageRepository;
import one.backbone.messagingassignment.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public MessageDto sendMessage(SendMessageRequest sendMessageRequest) {
        return null;
    }

    @Override
    public List<MessageDto> getMessages(GetMessageRequest request) {
        return null;
    }
}
