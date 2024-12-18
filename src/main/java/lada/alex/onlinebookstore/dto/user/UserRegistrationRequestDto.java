package lada.alex.onlinebookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lada.alex.onlinebookstore.validator.FieldMatch;

@FieldMatch(
        field = "password",
        repeatField = "repeatPassword",
        message = "Password and repeat password should be equals")
@Getter
@Setter
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    @Size(min = 5, max = 255)
    private String email;
    @NotBlank
    @Size(min = 8, max = 255)
    private String password;
    @NotBlank
    @Size(min = 8, max = 255)
    private String repeatPassword;
    @NotBlank
    @Size(min = 2, max = 255)
    private String firstName;
    @Size(min = 2, max = 255)
    @NotBlank
    private String lastName;
    @Size(min = 2, max = 255)
    private String shippingAddress;
}
