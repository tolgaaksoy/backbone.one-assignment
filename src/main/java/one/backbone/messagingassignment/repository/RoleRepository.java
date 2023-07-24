package one.backbone.messagingassignment.repository;

import one.backbone.messagingassignment.model.entity.RoleType;
import one.backbone.messagingassignment.model.entity.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository extends BaseRepository<UserRole, Long> {

    Set<UserRole> findAllByRoleTypeInAndDeletedFalse(List<RoleType> roleTypes);

}
