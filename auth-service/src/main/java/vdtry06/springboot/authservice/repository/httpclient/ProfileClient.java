package vdtry06.springboot.authservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vdtry06.springboot.authservice.dto.request.ProfileCreationRequest;
import vdtry06.springboot.authservice.dto.response.UserProfileResponse;

@FeignClient(name = "profile-service", url = "${app.services.profile}")
public interface ProfileClient {
    @PostMapping(value = "internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    UserProfileResponse createProfile(@RequestBody ProfileCreationRequest request);

    @GetMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    UserProfileResponse getAllProfiles();

    @DeleteMapping(value = "/internal/users/{profileId}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserProfileResponse deleteProfile(@PathVariable("profileId") String profileId);
}
