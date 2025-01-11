package vdtry06.springboot.profile.profile_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import vdtry06.springboot.profile.profile_service.dto.request.UserProfileUpdationRequest;
import vdtry06.springboot.profile.profile_service.dto.response.UserProfileResponse;
import vdtry06.springboot.profile.profile_service.service.UserProfileService;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserProfileService userProfileService;

//    @PostMapping("/users")
//    UserProfileResponse createProfile(@RequestBody UserProfileCreationRequest request) {
//        return userProfileService.createProfile(request);
//    }

    @GetMapping("/users/{profileId}")
    UserProfileResponse getProfile(@PathVariable String profileId) {
        return userProfileService.getProfile(profileId);
    }

    @PostMapping("/users/{profileId}")
    UserProfileResponse updateProfile(@PathVariable String profileId, @RequestBody UserProfileUpdationRequest request) {
        return userProfileService.updateProfile(profileId, request);
    }

}
