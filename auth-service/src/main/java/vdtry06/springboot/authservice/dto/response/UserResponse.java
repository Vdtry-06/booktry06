package vdtry06.springboot.authservice.dto.response;

import java.time.LocalDate;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String email;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    Set<RoleResponse> roles;
}
