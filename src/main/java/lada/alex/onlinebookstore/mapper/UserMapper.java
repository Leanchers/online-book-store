package lada.alex.onlinebookstore.mapper;

import lada.alex.onlinebookstore.config.MapperConfig;
import lada.alex.onlinebookstore.dto.user.UserRegistrationRequestDto;
import lada.alex.onlinebookstore.dto.user.UserResponseDto;
import lada.alex.onlinebookstore.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
