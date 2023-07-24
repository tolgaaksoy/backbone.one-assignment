package one.backbone.messagingassignment.service;


import one.backbone.messagingassignment.model.dto.UserDto;
import one.backbone.messagingassignment.model.dto.request.UpdateUserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The interface User service.
 */
public interface UserService {

    /**
     * Update user user dao.
     *
     * @param request the request
     * @return the user dao
     */
    UserDto updateUser(UpdateUserRequest request);

    /**
     * Delete user by id boolean.
     *
     * @param id the id
     * @return the boolean
     */
    boolean deleteUserById(Long id);

    /**
     * Gets user by id.
     *
     * @param id the id
     * @return the user by id
     */
    UserDto getUserById(Long id);


    List<UserDto> getAllUsers();

    Page<UserDto> getAllUsers(Pageable pageable);
}
