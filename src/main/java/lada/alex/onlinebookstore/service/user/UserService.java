package lada.alex.onlinebookstore.service.user;

import lada.alex.onlinebookstore.dto.user.UserRegistrationRequestDto;
import lada.alex.onlinebookstore.dto.user.UserResponseDto;
import lada.alex.onlinebookstore.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
