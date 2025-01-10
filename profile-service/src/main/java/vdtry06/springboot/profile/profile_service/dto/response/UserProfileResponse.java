package vdtry06.springboot.profile.profile_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE)
public class UserProfileResponse {
    String id;
    String email;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String city;
}
