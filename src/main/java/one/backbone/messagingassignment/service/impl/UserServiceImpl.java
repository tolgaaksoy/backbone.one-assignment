package one.backbone.messagingassignment.service.impl;

import lombok.extern.slf4j.Slf4j;
import one.backbone.messagingassignment.exception.UserNotFoundException;
import one.backbone.messagingassignment.mapper.UserMapper;
import one.backbone.messagingassignment.model.dto.UserDto;
import one.backbone.messagingassignment.model.dto.request.UpdateUserRequest;
import one.backbone.messagingassignment.model.entity.User;
import one.backbone.messagingassignment.repository.UserRepository;
import one.backbone.messagingassignment.service.UserMessageDetailsService;
import one.backbone.messagingassignment.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The implementation of the {@link UserService} interface.
 * This class provides methods for creating, updating, deleting and retrieving users.
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final UserMessageDetailsService userMessageDetailsService;

    /**
     * Constructs a new UserServiceImpl with the given user repository and user mapper.
     *
     * @param userRepository            the user repository
     * @param userMapper                the user mapper
     * @param userMessageDetailsService the user message details service
     */
    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           UserMessageDetailsService userMessageDetailsService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userMessageDetailsService = userMessageDetailsService;
    }

    /**
     * Updates an existing user with the given user details and returns the updated user.
     *
     * @param request the request containing the user details
     * @return the updated user
     * @throws UserNotFoundException if the user with the given ID is not found
     */
    @Override
    public UserDto updateUser(UpdateUserRequest request) {
        Optional<User> optionalUser = userRepository.findByIdAndDeletedFalse(request.getId());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(request.getId());
        }
        User user = userMapper.updateEntity(optionalUser.get(), request);
        return userMapper.toDto(userRepository.save(user));
    }


    /**
     * Deletes the user with the given ID.
     *
     * @param id the ID of the user to delete
     * @return true if the user was deleted successfully, false otherwise
     * @throws UserNotFoundException if the user with the given ID is not found
     */
    @Override
    public boolean deleteUserById(Long id) {
        Optional<User> optionalUser = userRepository.findByIdAndDeletedFalse(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        User user = optionalUser.get();
        user.setDeleted(true);
        userRepository.save(user);
        return true;
    }

    /**
     * Retrieves the user with the given ID.
     *
     * @param id the ID of the user to retrieve
     * @return the user with the given ID
     * @throws UserNotFoundException if the user with the given ID is not found
     */
    @Override
    public UserDto getUserById(Long id) {
        Optional<User> user = userRepository.findByIdAndDeletedFalse(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        UserDto userDto = userMapper.toDto(user.get());
        userDto.setAverageResponseTime(userMessageDetailsService.getAverageResponseTimeByUserId(id));
        return userDto;
    }

    /**
     * Retrieves all users.
     *
     * @return the list of all users
     */
    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAllByDeletedFalse();
        return users.stream().map(userMapper::toDto).toList();
    }

    /**
     * Retrieves all users.
     *
     * @param pageable the pageable
     * @return the list of all users
     */
    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAllByDeletedFalse(pageable);
        return new PageImpl<>(users.stream().map(userMapper::toDto).toList(), pageable, users.getTotalElements());
    }

}
