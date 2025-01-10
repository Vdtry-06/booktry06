package vdtry06.springboot.authservice.mapper;

import org.mapstruct.Mapper;
import vdtry06.springboot.authservice.dto.request.ProfileCreationRequest;
import vdtry06.springboot.authservice.dto.request.UserCreationRequest;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(UserCreationRequest request);
}
