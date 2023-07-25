package one.backbone.messagingassignment.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageFilterDto {
    private boolean isDeleted = false;
    private String content;
    private Long senderId;
    private Long recipientId;
    private LocalDateTime createdAt;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
}
