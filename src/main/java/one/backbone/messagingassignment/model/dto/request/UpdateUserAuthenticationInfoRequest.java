package one.backbone.messagingassignment.model.dto.request;


import jakarta.validation.constraints.Size;
import lombok.*;
import one.backbone.messagingassignment.model.entity.RoleType;

import java.util.List;

/**
 * The type Update user authentication info request.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserAuthenticationInfoRequest {

    private Long id;

    @Size(max = 255, message = "Username must be less than 255 characters...")
    private String username;

    @Size(max = 255, message = "Password must be less than 255 characters...")
    private String password;

    private List<RoleType> roles;

}
