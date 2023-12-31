package one.backbone.messagingassignment.controller;

import jakarta.validation.Valid;
import one.backbone.messagingassignment.model.dto.request.CreateUserRequest;
import one.backbone.messagingassignment.model.dto.request.LoginRequest;
import one.backbone.messagingassignment.model.dto.request.UpdateUserAuthenticationInfoRequest;
import one.backbone.messagingassignment.model.dto.response.LoginResponse;
import one.backbone.messagingassignment.model.dto.response.UserResponse;
import one.backbone.messagingassignment.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

/**
 * The type Auth controller.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Instantiates a new Auth controller.
     *
     * @param authService the auth service
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Login response entity.
     *
     * @param loginRequest the login request
     * @return the response entity
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.authenticateUser(loginRequest);
        response.setResponseStatus(200);
        response.setResponseTimestamp(Instant.now());
        response.setResponseMessage("Login Successful");
        return ResponseEntity.ok(response);
    }

    /**
     * Create user response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = UserResponse.builder()
                .user(authService.createUser(request))
                .responseStatus(201)
                .responseTimestamp(Instant.now())
                .responseMessage("User created successfully")
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update user response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PutMapping("/authInfo")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateUserAuthenticationInfoRequest request) {
        UserResponse response = UserResponse.builder()
                .user(authService.updateUserAuthenticationInformation(request))
                .responseStatus(200)
                .responseTimestamp(Instant.now())
                .responseMessage("User authentication information updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

}
