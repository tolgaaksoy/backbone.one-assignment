package one.backbone.messagingassignment.controller;

import one.backbone.messagingassignment.model.dto.request.SendMessageRequest;
import one.backbone.messagingassignment.model.dto.response.MessageResponse;
import one.backbone.messagingassignment.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/messages")
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody SendMessageRequest request) {
        MessageResponse response = MessageResponse.builder()
                .message(messageService.sendMessage(request))
                .responseStatus(HttpStatus.CREATED.value())
                .responseMessage("Message sent successfully...")
                .responseTimestamp(Instant.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/messages")
    public ResponseEntity<MessageResponse> retrieveMessagesByUserId(@PathVariable Long userId,
                                                                    @RequestParam(required = false) String dateFrom,
                                                                    @RequestParam(required = false) String dateTo) {
        MessageResponse response = MessageResponse.builder()
                .messageList(messageService.retrieveMessagesByUserId(userId, dateFrom, dateTo))
                .responseStatus(HttpStatus.OK.value())
                .responseMessage("Messages retrieved successfully...")
                .responseTimestamp(Instant.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
