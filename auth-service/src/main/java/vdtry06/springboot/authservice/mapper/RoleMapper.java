package vdtry06.springboot.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import vdtry06.springboot.authservice.dto.request.RoleRequest;
import vdtry06.springboot.authservice.dto.response.RoleResponse;
import vdtry06.springboot.authservice.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
