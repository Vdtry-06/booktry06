package vdtry06.springboot.profile.profile_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileUpdationRequest {
    String userId;
    String email;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String city;
}
