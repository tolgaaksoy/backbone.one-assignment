package one.backbone.messagingassignment.service.impl;

import lombok.extern.slf4j.Slf4j;
import one.backbone.messagingassignment.model.entity.Message;
import one.backbone.messagingassignment.model.entity.User;
import one.backbone.messagingassignment.repository.MessageRepository;
import one.backbone.messagingassignment.service.MessageWSService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class MessageWSWSServiceImpl implements MessageWSService {

    private final MessageRepository messageRepository;

    public MessageWSWSServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message sendMessage(String messageText, User sender, User recipient) {
        Message message = new Message();
        message.setContent(messageText);
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setCreatedAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesByUserId(Long userId) {
        User user = new User();
        user.setId(userId);
        return messageRepository.findBySenderOrRecipientOrderByCreatedAt(user, user);
    }

    public List<Message> getMessagesByUserIdAndDateRange(Long userId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        User user = new User();
        user.setId(userId);

        List<Message> sentMessages = messageRepository.findByCreatedAtBetweenAndSender(dateFrom, dateTo, user);
        List<Message> receivedMessages = messageRepository.findByCreatedAtBetweenAndRecipient(dateFrom, dateTo, user);

        // Combine the sent and received messages and sort by createdAt
        List<Message> allMessages = new ArrayList<>(sentMessages);
        allMessages.addAll(receivedMessages);
        allMessages.sort(Comparator.comparing(Message::getCreatedAt));

        return allMessages;
    }
}
