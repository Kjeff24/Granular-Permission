package com.bexos.granular_permission.services.interfaces;

import com.bexos.granular_permission.dto.AuthRequest;
import com.bexos.granular_permission.dto.AuthenticationResponse;
import com.bexos.granular_permission.dto.InviteRequest;
import com.bexos.granular_permission.dto.PermissionRequest;
import com.bexos.granular_permission.dto.RegisterRequest;
import com.bexos.granular_permission.dto.UserResponse;
import com.bexos.granular_permission.models.User;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthRequest request);

    List<User> getAllUsers();

    UserResponse addPermissionToUser(PermissionRequest request) throws BadRequestException;

    User removePermissionFromUser(PermissionRequest request) throws BadRequestException;

    User getAuthenticatedUser();

    void deleteUser(int userId);

    User inviteUser(InviteRequest inviteRequest);
}
