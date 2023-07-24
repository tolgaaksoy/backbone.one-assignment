package one.backbone.messagingassignment.service;


import one.backbone.messagingassignment.model.dto.UserDto;
import one.backbone.messagingassignment.model.dto.request.CreateUserRequest;
import one.backbone.messagingassignment.model.dto.request.LoginRequest;
import one.backbone.messagingassignment.model.dto.request.UpdateUserAuthenticationInfoRequest;
import one.backbone.messagingassignment.model.dto.response.LoginResponse;

/**
 * The interface Auth service.
 */
public interface AuthService {

    /**
     * Authenticate user login response.
     *
     * @param loginRequest the login request
     * @return the login response
     */
    LoginResponse authenticateUser(LoginRequest loginRequest);

    /**
     * Create user user dao.
     *
     * @param request the request
     * @return the user dao
     */
    UserDto createUser(CreateUserRequest request);

    /**
     * Update user authentication information user dao.
     *
     * @param request the request
     * @return the user dao
     */
    UserDto updateUserAuthenticationInformation(UpdateUserAuthenticationInfoRequest request);
}
