package one.backbone.messagingassignment.model.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMessageRequest {

    private Long recipientId;

    private Long senderId;

    private LocalDateTime from;

    private LocalDateTime to;
}
