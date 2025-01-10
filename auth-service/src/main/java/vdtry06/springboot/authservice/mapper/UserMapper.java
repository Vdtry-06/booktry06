package vdtry06.springboot.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import vdtry06.springboot.authservice.dto.request.UserCreationRequest;
import vdtry06.springboot.authservice.dto.request.UserUpdationRequest;
import vdtry06.springboot.authservice.dto.response.UserResponse;
import vdtry06.springboot.authservice.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdationRequest request);
}
