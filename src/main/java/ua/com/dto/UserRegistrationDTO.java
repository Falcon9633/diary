package ua.com.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import ua.com.anotation.ValidEmail;
import ua.com.entity.Authority;

import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserRegistrationDTO {

    @NotBlank
    private String userType;
    @NotBlank
    @Size(min = 2, max = 16)
    private String name;
    @NotBlank
    @Size(min = 2, max = 16)
    private String surname;
    @NotBlank
    @ValidEmail
    private String email;
    private String password;
    private String matchingPassword;
    private Authority authority;

}
