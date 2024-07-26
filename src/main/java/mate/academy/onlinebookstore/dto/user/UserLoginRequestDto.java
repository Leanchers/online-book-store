package mate.academy.onlinebookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotBlank
        @Email
        @Size(min = 5, max = 255)
        String email,
        @NotBlank
        @Size(min = 8, max = 255)
        String password
) {
}
