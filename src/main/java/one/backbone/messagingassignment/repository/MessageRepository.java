package one.backbone.messagingassignment.repository;

import one.backbone.messagingassignment.model.entity.Message;
import one.backbone.messagingassignment.model.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends BaseRepository<Message, Long>, JpaSpecificationExecutor<Message> {
    List<Message> findBySenderOrRecipientOrderByCreatedAt(User sender, User recipient);

    List<Message> findByCreatedAtBetweenAndSender(LocalDateTime dateFrom, LocalDateTime dateTo, User sender);

    List<Message> findByCreatedAtBetweenAndRecipient(LocalDateTime dateFrom, LocalDateTime dateTo, User recipient);
}
