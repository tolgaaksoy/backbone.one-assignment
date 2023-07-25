package one.backbone.messagingassignment.model.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {

    private String message;
    private Long senderId;
    private Long recipientId;

}
