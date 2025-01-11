package vdtry06.springboot.profile.profile_service.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vdtry06.springboot.profile.profile_service.dto.request.UserProfileCreationRequest;
import vdtry06.springboot.profile.profile_service.dto.request.UserProfileUpdationRequest;
import vdtry06.springboot.profile.profile_service.dto.response.UserProfileResponse;
import vdtry06.springboot.profile.profile_service.entity.UserProfile;
import vdtry06.springboot.profile.profile_service.mapper.UserProfileMapper;
import vdtry06.springboot.profile.profile_service.repository.UserProfileRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    public UserProfileResponse createProfile(UserProfileCreationRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile =  userProfileRepository.save(userProfile);

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getProfile(String profileId) {
        UserProfile userProfile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public List<UserProfileResponse> getAllProfiles() {
        return userProfileRepository.findAll().stream().map(userProfileMapper::toUserProfileResponse).toList();
    }

    public UserProfileResponse updateProfile(String profileId, UserProfileUpdationRequest request) {
        UserProfile userProfile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        userProfileMapper.updateUserProfile(userProfile, request);
        userProfile =  userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public void deleteProfile(String profileId) {
        userProfileRepository.deleteById(profileId);
    }

}
