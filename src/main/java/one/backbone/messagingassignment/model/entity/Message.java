package one.backbone.messagingassignment.model.entity;

import jakarta.persistence.Entity;
import lombok.*;

import java.util.UUID;

@Entity(name = "message")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message extends BaseEntity {

    private String content;

    private UUID senderId;

    private UUID recipientId;
}
