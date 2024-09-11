package com.bexos.granular_permission.services.impl;

import com.bexos.granular_permission.config.jwt_config.JwtServiceImpl;
import com.bexos.granular_permission.dto.AuthRequest;
import com.bexos.granular_permission.dto.AuthenticationResponse;
import com.bexos.granular_permission.dto.InviteRequest;
import com.bexos.granular_permission.dto.PermissionRequest;
import com.bexos.granular_permission.dto.RegisterRequest;
import com.bexos.granular_permission.dto.UserResponse;
import com.bexos.granular_permission.handlers.NotFoundException;
import com.bexos.granular_permission.handlers.UnAuthorizedException;
import com.bexos.granular_permission.models.Permission;
import com.bexos.granular_permission.models.RoleEnum;
import com.bexos.granular_permission.models.User;
import com.bexos.granular_permission.repositories.UserRepository;
import com.bexos.granular_permission.services.interfaces.UserService;
import com.bexos.granular_permission.utils.UserMapper;
import com.bexos.granular_permission.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(RoleEnum.valueOf(request.role()))
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var user = userRepository.findByEmail(request.email())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .authorities(user.getAuthorities())
                .build();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserResponse addPermissionToUser(PermissionRequest request) throws BadRequestException {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (canAddPermission(user.getRole())) {
            throw new BadRequestException("You do not have permission to add this permission");
        }

        if (verifyPermission(user.getRole(), request.permission())) {
            throw new BadRequestException("Invalid permission");
        }

        if (user.getPermissions().add(Permission.valueOf(request.permission()))) {
//            return userMapper.toUserResponse(userRepository.save(user));
            System.out.println("Permission added");
            return UserResponse.builder().build();
        }


//        return userMapper.toUserResponse(user);
        return UserResponse.builder().build();
    }

    public User removePermissionFromUser(PermissionRequest request) throws BadRequestException {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (canAddPermission(user.getRole())) {
            throw new BadRequestException("You do not have permission to add this permission");
        }

        if (verifyPermission(user.getRole(), request.permission())) {
            throw new BadRequestException("Invalid permission");
        }

        if (user.getPermissions().remove(Permission.valueOf(request.permission()))) {
            return userRepository.save(user);
        }

        return user;
    }

    private boolean verifyPermission(RoleEnum role, String requestedPermission) {
        Set<Permission> allowedPermissions = switch (role) {
            case CO_ADMIN -> Set.of(
                    Permission.CO_ADMIN_INVITE_USER,
                    Permission.CO_ADMIN_VIEW_USERS,
                    Permission.CO_ADMIN_DELETE_USERS
            );
            case CO_ORGANIZER -> Set.of(
                    Permission.CO_ORGANIZER_CREATE_EVENT,
                    Permission.CO_ORGANIZER_EDIT_EVENT,
                    Permission.CO_ORGANIZER_DELETE_EVENT,
                    Permission.CO_ORGANIZER_INVITE_USERS
            );
            default -> Set.of();
        };

        return !allowedPermissions.contains(Permission.valueOf(requestedPermission));
    }

    private boolean canAddPermission(RoleEnum targetUserRole) {
        User currentUser = getAuthenticatedUser();
        if (currentUser.getRole() == RoleEnum.ADMIN) {
            return targetUserRole != RoleEnum.CO_ADMIN;
        } else if (currentUser.getRole() == RoleEnum.ORGANIZER) {
            return targetUserRole != RoleEnum.CO_ORGANIZER;
        }
        return true;
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (User) authentication.getPrincipal();
        }
        throw new UsernameNotFoundException("No authenticated user found");
    }

    public void deleteUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getRole().equals(RoleEnum.ADMIN)) {
            throw new UnAuthorizedException("You do not have permission to delete this user");
        }

        if (getAuthenticatedUser().getRole().equals(RoleEnum.ORGANIZER) && user.getRole().equals(RoleEnum.CO_ADMIN) ||
                getAuthenticatedUser().getRole().equals(RoleEnum.ORGANIZER) && user.getRole().equals(RoleEnum.ATTENDEE)) {
            throw new UnAuthorizedException("You do not have permission to delete this user");
        }

        userRepository.delete(user);
    }

    public User inviteUser(InviteRequest inviteRequest) {
        User user = User.builder()
                .email(inviteRequest.email())
                .role(getAuthenticatedUser().getRole().equals(RoleEnum.ADMIN) ? RoleEnum.CO_ADMIN : RoleEnum.CO_ORGANIZER)
                .manager(getAuthenticatedUser().getManager() != null ? getAuthenticatedUser().getManager() : getAuthenticatedUser())
                .referrer(getAuthenticatedUser())
                .password(UserUtils.generateRandomPassword())
                .username(UserUtils.extractUsername(inviteRequest.email()))
                .build();

        return userRepository.save(user);
    }

}
