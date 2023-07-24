package one.backbone.messagingassignment.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

/**
 * The type Role dao.
 */
@Entity(name = "auth_role")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

}
