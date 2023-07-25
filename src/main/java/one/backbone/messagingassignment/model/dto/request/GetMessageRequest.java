package one.backbone.messagingassignment.model.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMessageRequest {

    private Long recipientId;
    private Long senderId;

    private String from;
    private String to;

}
