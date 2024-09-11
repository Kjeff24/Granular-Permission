package com.bexos.granular_permission.controllers;

import com.bexos.granular_permission.dto.InviteRequest;
import com.bexos.granular_permission.models.User;
import com.bexos.granular_permission.services.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasAnyRole('ADMIN', 'C0_ADMIN', 'ORGANIZER')")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('admin:view_users', 'co_admin:view_users')")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAnyAuthority('admin:delete_users', 'co_admin:delete_users', 'organizer:delete_users')")
    public void deleteUser(@PathVariable("userId") int userId) {
        userService.deleteUser(userId);
    }

    @PostMapping("/invite")
    @PreAuthorize("hasAnyAuthority('admin:invite_users', 'co_admin:invite_users')")
    public ResponseEntity<User> inviteUser(@Valid @RequestBody InviteRequest inviteRequest) {
        return new ResponseEntity<>(userService.inviteUser(inviteRequest), HttpStatus.CREATED);
    }


}