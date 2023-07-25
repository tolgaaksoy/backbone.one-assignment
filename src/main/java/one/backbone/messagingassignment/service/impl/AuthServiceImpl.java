package one.backbone.messagingassignment.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import one.backbone.messagingassignment.exception.UserNotFoundException;
import one.backbone.messagingassignment.exception.UsernameAlreadyExistException;
import one.backbone.messagingassignment.mapper.UserMapper;
import one.backbone.messagingassignment.model.dto.UserDto;
import one.backbone.messagingassignment.model.dto.request.CreateUserRequest;
import one.backbone.messagingassignment.model.dto.request.LoginRequest;
import one.backbone.messagingassignment.model.dto.request.UpdateUserAuthenticationInfoRequest;
import one.backbone.messagingassignment.model.dto.response.LoginResponse;
import one.backbone.messagingassignment.model.entity.Token;
import one.backbone.messagingassignment.model.entity.TokenType;
import one.backbone.messagingassignment.model.entity.User;
import one.backbone.messagingassignment.repository.RoleRepository;
import one.backbone.messagingassignment.repository.TokenRepository;
import one.backbone.messagingassignment.repository.UserRepository;
import one.backbone.messagingassignment.security.jwt.JwtUtils;
import one.backbone.messagingassignment.security.service.UserDetailsImpl;
import one.backbone.messagingassignment.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The implementation of the {@link AuthService} interface.
 * This class provides methods for user authentication, creation and updating user authentication information.
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService, LogoutHandler {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final RoleRepository roleRepository;

    /**
     * Instantiates a new Auth service.
     *
     * @param authenticationManager the authentication manager
     * @param userRepository        the user repository
     * @param encoder               the encoder
     * @param jwtUtils              the jwt utils
     * @param tokenRepository
     * @param userMapper            the user mapper
     * @param roleRepository        the role repository
     */
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           PasswordEncoder encoder,
                           JwtUtils jwtUtils,
                           TokenRepository tokenRepository,
                           UserMapper userMapper,
                           RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.tokenRepository = tokenRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

    /**
     * Authenticates a user with the given login credentials and returns a JWT token and user roles.
     *
     * @param loginRequest the login request containing the username and password
     * @return a {@link LoginResponse} object containing the JWT token and user roles
     */
    @Override
    public LoginResponse authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String jwt = jwtUtils.generateJwtToken(authentication);
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow();

        revokeAllUserTokens(user);
        saveUserToken(user, jwt);

        return LoginResponse.builder()
                .token(jwt)
                .roles(roles)
                .username(userDetails.getUsername())
                .build();
    }

    /**
     * Creates a new user with the given user details and returns the created user.
     *
     * @param request the request containing the user details
     * @return the created user
     */
    @Override
    public UserDto createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistException(request.getUsername());
        }
        User user = userMapper.toEntity(request);
        user.setRoles(roleRepository.findAllByRoleTypeInAndDeletedFalse(request.getRoles()));
        user.setPassword(encoder.encode(request.getPassword()));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String jwt = jwtUtils.parseJwt(request);
        Optional<Token> optionalToken = tokenRepository.findByAccessToken(jwt);
        if (optionalToken.isPresent()) {
            Token token = optionalToken.get();
            token.setExpired(true);
            token.setRevoked(true);
            tokenRepository.save(token);
            SecurityContextHolder.clearContext();
        }
    }

    /**
     * Updates the authentication information of an existing user with the given user details and returns the updated user.
     *
     * @param request the request containing the user details
     * @return the updated user
     * @throws UserNotFoundException if the user with the given ID is not found
     */
    @Override
    public UserDto updateUserAuthenticationInformation(UpdateUserAuthenticationInfoRequest request) {
        Optional<User> optionalUser = userRepository.findByIdAndDeletedFalse(request.getId());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(request.getId());
        }
        if (request.getUsername() != null && userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistException(request.getUsername());
        }
        User user = userMapper.updateEntity(optionalUser.get(), request);
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            user.setRoles(roleRepository.findAllByRoleTypeInAndDeletedFalse(request.getRoles()));
        }
        return userMapper.toDto(userRepository.save(user));
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .accessToken(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}