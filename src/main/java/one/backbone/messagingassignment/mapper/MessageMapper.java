package one.backbone.messagingassignment.mapper;

import one.backbone.messagingassignment.model.dto.MessageDto;
import one.backbone.messagingassignment.model.entity.Message;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

@Component
@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MessageMapper {

    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "recipientId", source = "recipient.id")
    @Mapping(target = "message", source = "content")
    MessageDto toDto(Message message);

}
