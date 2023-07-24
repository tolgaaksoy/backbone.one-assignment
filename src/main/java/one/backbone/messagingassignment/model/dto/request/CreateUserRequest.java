package one.backbone.messagingassignment.model.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import one.backbone.messagingassignment.model.entity.RoleType;

import java.util.List;

/**
 * The type Create user request.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "Username is required...")
    @Size(max = 255, message = "Username must be less than 255 characters...")
    private String username;

    @NotBlank(message = "Password is required...")
    @Size(max = 255, message = "Password must be less than 255 characters...")
    private String password;

    private List<RoleType> roles;

    @Size(max = 255, message = "Name must be less than 255 characters...")
    private String name;

}
