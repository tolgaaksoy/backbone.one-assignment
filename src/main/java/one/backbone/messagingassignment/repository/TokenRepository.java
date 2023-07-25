package one.backbone.messagingassignment.repository;

import one.backbone.messagingassignment.model.entity.Token;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends BaseRepository<Token, Long> {

    @Query(value = """
            SELECT t FROM token t INNER JOIN auth_user
              u ON t.user.id = u.id
              WHERE u.id = :id AND (t.expired = false OR t.revoked = false)
              """)
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByAccessToken(String token);

}
