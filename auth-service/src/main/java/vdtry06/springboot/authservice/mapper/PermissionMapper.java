package vdtry06.springboot.authservice.mapper;

import org.mapstruct.Mapper;

import vdtry06.springboot.authservice.dto.request.PermissionRequest;
import vdtry06.springboot.authservice.dto.response.PermissionResponse;
import vdtry06.springboot.authservice.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
