package one.backbone.messagingassignment.repository;

import one.backbone.messagingassignment.model.entity.Message;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends BaseRepository<Message, Long> {
}
