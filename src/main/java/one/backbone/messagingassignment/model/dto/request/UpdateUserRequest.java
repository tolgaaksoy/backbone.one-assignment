package one.backbone.messagingassignment.model.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * The type Update user request.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    private Long id;

    @Size(max = 255, message = "Name must be less than 255 characters...")
    private String name;

}
