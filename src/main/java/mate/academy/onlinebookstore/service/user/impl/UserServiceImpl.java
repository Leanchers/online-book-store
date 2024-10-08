package mate.academy.onlinebookstore.service.user.impl;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.user.UserRegistrationRequestDto;
import mate.academy.onlinebookstore.dto.user.UserResponseDto;
import mate.academy.onlinebookstore.exception.EntityNotFoundException;
import mate.academy.onlinebookstore.exception.RegistrationException;
import mate.academy.onlinebookstore.mapper.UserMapper;
import mate.academy.onlinebookstore.model.Role;
import mate.academy.onlinebookstore.model.Role.RoleName;
import mate.academy.onlinebookstore.model.User;
import mate.academy.onlinebookstore.repository.role.RoleRepository;
import mate.academy.onlinebookstore.repository.user.UserRepository;
import mate.academy.onlinebookstore.service.shoppingcart.ShoppingCartService;
import mate.academy.onlinebookstore.service.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartService shoppingCartService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(String
                .format("User with this email: %s already exists", requestDto.getEmail()));
        }
        User user = userMapper.toModel(requestDto);
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException("Can't find role by name: "
                + RoleName.ROLE_USER)));
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        shoppingCartService.createShoppingCart(user);
        return userMapper.toDto(userRepository.save(user));
    }
}
