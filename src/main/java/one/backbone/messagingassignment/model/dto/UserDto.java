package one.backbone.messagingassignment.model.dto;

import lombok.*;
import one.backbone.messagingassignment.model.entity.RoleType;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String username;

    private List<RoleType> roles;

    private String name;


}
