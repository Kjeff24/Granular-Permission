package com.bexos.granular_permission.controllers;

import com.bexos.granular_permission.dto.PermissionRequest;
import com.bexos.granular_permission.dto.UserResponse;
import com.bexos.granular_permission.models.User;
import com.bexos.granular_permission.services.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/change-permission")
@PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'CO_ADMIN', 'CO_ORGANIZER')")
public class SharedController {
    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('admin_grant_permissions', 'organizer_grant_permissions')")
    @PutMapping("/add")
    public ResponseEntity<UserResponse> addPermission(@Valid @RequestBody PermissionRequest request) throws BadRequestException {
        return ResponseEntity.ok(userService.addPermissionToUser(request));
    }

    @PreAuthorize("hasAnyAuthority('admin_grant_permissions', 'organizer_grant_permissions')")
    @PutMapping("/remove")
    public ResponseEntity<User> removePermission(@Valid @RequestBody PermissionRequest request) throws BadRequestException {
        User updatedUser = userService.removePermissionFromUser(request);
        return ResponseEntity.ok(updatedUser);
    }

}
