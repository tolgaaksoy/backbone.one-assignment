package one.backbone.messagingassignment.mapper;

import one.backbone.messagingassignment.model.dto.UserDto;
import one.backbone.messagingassignment.model.dto.request.CreateUserRequest;
import one.backbone.messagingassignment.model.dto.request.UpdateUserAuthenticationInfoRequest;
import one.backbone.messagingassignment.model.dto.request.UpdateUserRequest;
import one.backbone.messagingassignment.model.entity.RoleType;
import one.backbone.messagingassignment.model.entity.User;
import one.backbone.messagingassignment.model.entity.UserRole;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The interface User mapper.
 */
@Component
@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {UserRole.class, Collectors.class})
public interface UserMapper {

    /**
     * To entity user dao.
     *
     * @param request the request
     * @return the user dao
     */
    @Mapping(target = "roles", ignore = true)
    User toEntity(CreateUserRequest request);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User updateEntity(@MappingTarget User user, UpdateUserRequest request);


    @Mapping(target = "name", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User updateEntity(@MappingTarget User user, UpdateUserAuthenticationInfoRequest request);

    default List<RoleType> mapUserRoleToRoleTypeList(Set<UserRole> userRoles) {
        return userRoles.stream()
                .map(UserRole::getRoleType)
                .toList();
    }

    @Mapping(target = "roles", expression = "java(mapUserRoleToRoleTypeList(user.getRoles()))")
    UserDto toDto(User user);

}
