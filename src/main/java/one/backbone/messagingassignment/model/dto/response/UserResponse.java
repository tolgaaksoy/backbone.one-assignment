package one.backbone.messagingassignment.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import one.backbone.messagingassignment.model.dto.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * The type User response.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse extends BaseResponse {

    /**
     * The User list.
     */
    private List<UserDto> userList;
    private Page<UserDto> userPage;
    private UserDto user;

}
