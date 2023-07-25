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
import one.backbone.messagingassignment.service.util.DateUtil;
import one.backbone.messagingassignment.specification.MessageSpecification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

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

}
