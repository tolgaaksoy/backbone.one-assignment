package one.backbone.messagingassignment.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private Long id;

    private String message;
    private Long senderId;
    private Long recipientId;

}
