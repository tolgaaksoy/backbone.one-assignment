package one.backbone.messagingassignment.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import one.backbone.messagingassignment.model.dto.MessageDto;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse extends BaseResponse {

    private MessageDto message;
    private List<MessageDto> messageList;

}
