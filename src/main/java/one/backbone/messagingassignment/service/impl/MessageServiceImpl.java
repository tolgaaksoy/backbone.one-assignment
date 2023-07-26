package one.backbone.messagingassignment.service.impl;

import lombok.extern.slf4j.Slf4j;
import one.backbone.messagingassignment.exception.UserNotFoundException;
import one.backbone.messagingassignment.mapper.MessageMapper;
import one.backbone.messagingassignment.model.dto.MessageDto;
import one.backbone.messagingassignment.model.dto.MessageFilterDto;
import one.backbone.messagingassignment.model.dto.request.SendMessageRequest;
import one.backbone.messagingassignment.model.entity.Message;
import one.backbone.messagingassignment.model.entity.User;
import one.backbone.messagingassignment.repository.MessageRepository;
import one.backbone.messagingassignment.repository.UserRepository;
import one.backbone.messagingassignment.service.MessageService;
import one.backbone.messagingassignment.service.UserMessageDetailsService;
import one.backbone.messagingassignment.service.util.DateUtil;
import one.backbone.messagingassignment.specification.MessageSpecification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService, UserMessageDetailsService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public MessageServiceImpl(UserRepository userRepository,
                              MessageRepository messageRepository,
                              MessageMapper messageMapper) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    @Override
    public MessageDto sendMessage(SendMessageRequest request) {
        Optional<User> sender = userRepository.findById(request.getSenderId());
        if (sender.isEmpty()) {
            log.error("Sender with id {} not found", request.getSenderId());
            throw new UserNotFoundException("Sender not found with id " + request.getSenderId());
        }
        Optional<User> recipient = userRepository.findById(request.getRecipientId());
        if (recipient.isEmpty()) {
            log.error("Recipient with id {} not found", request.getRecipientId());
            throw new UserNotFoundException("Recipient not found with id " + request.getRecipientId());
        }

        Message message = Message.builder()
                .content(request.getMessage())
                .sender(sender.get())
                .recipient(recipient.get())
                .createdAt(LocalDateTime.now())
                .build();

        return messageMapper.toDto(messageRepository.save(message));
    }

    @Override
    public List<MessageDto> retrieveMessagesByUserId(Long userId, String dateFrom, String dateTo) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            log.error("User with id {} not found", userId);
            throw new UserNotFoundException("User not found with id " + userId);
        }

        MessageFilterDto filter = MessageFilterDto.builder()
                .recipientId(userId)
                .dateFrom(DateUtil.getLocalDateTimeFromISO8601String(dateFrom, false))
                .dateTo(DateUtil.getLocalDateTimeFromISO8601String(dateTo, false))
                .build();

        List<Message> messages = messageRepository.findAll(new MessageSpecification(filter));

        return messages.stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(messageMapper::toDto)
                .collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
    }

    @Override
    public Float getAverageResponseTimeByUserId(Long userId) {
        MessageFilterDto senderFilter = MessageFilterDto.builder()
                .senderId(userId)
                .build();
        List<Message> sentMessages = messageRepository.findAll(new MessageSpecification(senderFilter));

        if (sentMessages.isEmpty()) {
            return 0f;
        }

        MessageFilterDto recipientFilter = MessageFilterDto.builder()
                .recipientId(userId)
                .build();
        List<Message> receivedMessages = messageRepository.findAll(new MessageSpecification(recipientFilter));

        if (receivedMessages.isEmpty()) {
            return 0f;
        }

        // Combine sent and received messages into a single list
        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(receivedMessages);

        // Sort all messages by createdAt timestamp
        allMessages.sort(Comparator.comparing(Message::getCreatedAt));

        // Calculate the response times and store them in a list
        List<Long> responseTimes = new ArrayList<>();
        Map<Long, Message> lastMessageMap = new HashMap<>();

        for (Message message : allMessages) {
            // Check if the user is the sender or the recipient
            if (message.getSender().getId().equals(userId)) {
                // User sent the message
                Message lastMessage = lastMessageMap.getOrDefault(message.getRecipient().getId(), null);
                if (lastMessage != null) {
                    // Calculate response time and add it to the list
                    long responseTime = ChronoUnit.SECONDS.between(lastMessage.getCreatedAt(), message.getCreatedAt());
                    responseTimes.add(responseTime);
                }
                lastMessageMap.put(message.getRecipient().getId(), message);
            } else if (message.getRecipient().getId().equals(userId)) {
                // User received the message
                Message lastMessage = lastMessageMap.getOrDefault(message.getSender().getId(), null);
                if (lastMessage != null) {
                    // Calculate response time and add it to the list
                    long responseTime = ChronoUnit.SECONDS.between(lastMessage.getCreatedAt(), message.getCreatedAt());
                    responseTimes.add(responseTime);
                }
                lastMessageMap.put(message.getSender().getId(), message);
            }
        }

        // Calculate the average response time
        if (responseTimes.isEmpty()) {
            return 0f;
        }
        double averageResponseTime = responseTimes.stream()
                .mapToLong(Long::intValue)
                .average()
                .orElse(0);
        return (float) averageResponseTime;
    }


}
