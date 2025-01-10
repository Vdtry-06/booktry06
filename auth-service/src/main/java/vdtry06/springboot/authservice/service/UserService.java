package vdtry06.springboot.authservice.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vdtry06.springboot.authservice.constant.PredefinedRole;
import vdtry06.springboot.authservice.dto.request.UserCreationRequest;
import vdtry06.springboot.authservice.dto.request.UserUpdationRequest;
import vdtry06.springboot.authservice.dto.response.UserResponse;
import vdtry06.springboot.authservice.entity.Role;
import vdtry06.springboot.authservice.entity.User;
import vdtry06.springboot.authservice.exception.AppException;
import vdtry06.springboot.authservice.exception.ErrorCode;
import vdtry06.springboot.authservice.mapper.UserMapper;
import vdtry06.springboot.authservice.repository.RoleRepository;
import vdtry06.springboot.authservice.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USERNAME_EXISTED);
        if (userRepository.existsByEmail(request.getEmail())) throw new AppException(ErrorCode.EMAIL_EXISTED);
        if (userRepository.existsByPassword(request.getPassword())) throw new AppException(ErrorCode.PASSWORD_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        log.info("User created: " + user);
        return userMapper.toUserResponse(user);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(String userId, UserUpdationRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (userRepository.existsByEmail(request.getEmail())) throw new AppException(ErrorCode.EMAIL_EXISTED);
        if (userRepository.existsByPassword(request.getPassword())) throw new AppException(ErrorCode.PASSWORD_EXISTED);

        log.info("Updating user with id: " + userId);
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("Getting all users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id) {
        log.info("Getting user with id: " + id);
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            log.info("User not existed!");
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        log.info("Deleting user {}", id);
        userRepository.deleteById(id);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }
}
