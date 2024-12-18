package lada.alex.onlinebookstore.service.user.impl;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lada.alex.onlinebookstore.dto.user.UserRegistrationRequestDto;
import lada.alex.onlinebookstore.dto.user.UserResponseDto;
import lada.alex.onlinebookstore.exception.EntityNotFoundException;
import lada.alex.onlinebookstore.exception.RegistrationException;
import lada.alex.onlinebookstore.mapper.UserMapper;
import lada.alex.onlinebookstore.model.Role;
import lada.alex.onlinebookstore.model.Role.RoleName;
import lada.alex.onlinebookstore.model.User;
import lada.alex.onlinebookstore.repository.role.RoleRepository;
import lada.alex.onlinebookstore.repository.user.UserRepository;
import lada.alex.onlinebookstore.service.shoppingcart.ShoppingCartService;
import lada.alex.onlinebookstore.service.user.UserService;
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
