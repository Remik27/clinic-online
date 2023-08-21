package pl.zajavka.infrastructure.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.zajavka.domain.Doctor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class UserDto {

    @Size(min = 5, max = 32)
    private String username;

    @Size(min = 5)
    @Pattern(regexp = ".*\\d.*", message = "Hasło musi zawierać przynajmniej jedną liczbę")
    private String password;

    @Email
    private String email;

    private String name;

    private String surname;

    @Size(min = 11, max = 11)
    private String pesel;

    private Roles role;

    private Doctor.Specialization specialization;
}
