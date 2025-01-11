package vdtry06.springboot.profile.profile_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import vdtry06.springboot.profile.profile_service.dto.request.UserProfileCreationRequest;
import vdtry06.springboot.profile.profile_service.dto.response.UserProfileResponse;
import vdtry06.springboot.profile.profile_service.service.UserProfileService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalUserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/internal/users")
    UserProfileResponse createProfile(@RequestBody UserProfileCreationRequest request) {
        return userProfileService.createProfile(request);
    }

    @GetMapping("/internal/users")
    List<UserProfileResponse> getAllProfiles() {
        return userProfileService.getAllProfiles();
    }

    @DeleteMapping("/internal/users/{profileId}")
    String deleteProfile(@PathVariable String profileId) {
        userProfileService.deleteProfile(profileId);
        return "user profile deleted";
    }
}
