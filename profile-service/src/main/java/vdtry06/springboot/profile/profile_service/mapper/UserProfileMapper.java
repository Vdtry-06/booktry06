package vdtry06.springboot.profile.profile_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import vdtry06.springboot.profile.profile_service.dto.request.UserProfileCreationRequest;
import vdtry06.springboot.profile.profile_service.dto.request.UserProfileUpdationRequest;
import vdtry06.springboot.profile.profile_service.dto.response.UserProfileResponse;
import vdtry06.springboot.profile.profile_service.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(UserProfileCreationRequest request);
    UserProfileResponse toUserProfileResponse(UserProfile entity);

    void updateUserProfile(@MappingTarget UserProfile userProfile, UserProfileUpdationRequest request);
}
