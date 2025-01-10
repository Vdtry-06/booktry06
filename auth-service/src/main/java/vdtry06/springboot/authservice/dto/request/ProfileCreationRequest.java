package vdtry06.springboot.authservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vdtry06.springboot.authservice.validator.BirthConstraint;
import vdtry06.springboot.authservice.validator.EmailConstraint;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreationRequest {

    @Size(min = 5, message = "USERNAME_INVALID")
    String username;

    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;

    @EmailConstraint(message = "EMAIL_INVALID")
    @NotEmpty(message = "EMAIL_NOT_EMPTY")
    String email;

    String firstName;
    String lastName;

    @BirthConstraint(min = 16, message = "INVALID_BIRTHDAY")
    LocalDate dateOfBirth;
}
