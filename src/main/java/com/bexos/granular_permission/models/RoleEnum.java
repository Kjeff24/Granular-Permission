package com.bexos.granular_permission.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bexos.granular_permission.models.Permission.ADMIN_DELETE_USERS;
import static com.bexos.granular_permission.models.Permission.ADMIN_GRANT_PERMISSION;
import static com.bexos.granular_permission.models.Permission.ADMIN_INVITE_USER;
import static com.bexos.granular_permission.models.Permission.ADMIN_VIEW_USERS;
import static com.bexos.granular_permission.models.Permission.ORGANIZER_CREATE_EVENT;
import static com.bexos.granular_permission.models.Permission.ORGANIZER_DELETE_EVENT;
import static com.bexos.granular_permission.models.Permission.ORGANIZER_DELETE_USERS;
import static com.bexos.granular_permission.models.Permission.ORGANIZER_EDIT_EVENT;
import static com.bexos.granular_permission.models.Permission.ORGANIZER_GRANT_PERMISSION;
import static com.bexos.granular_permission.models.Permission.ORGANIZER_INVITE_USERS;
import static com.bexos.granular_permission.models.Permission.ORGANIZER_VIEW_EVENT;

@Getter
@RequiredArgsConstructor
public enum RoleEnum {
    ADMIN(Set.of(
            ADMIN_INVITE_USER,
            ADMIN_VIEW_USERS,
            ADMIN_DELETE_USERS,
            ADMIN_GRANT_PERMISSION)
    ),
    ORGANIZER(Set.of(
            ORGANIZER_CREATE_EVENT,
            ORGANIZER_EDIT_EVENT,
            ORGANIZER_VIEW_EVENT,
            ORGANIZER_DELETE_EVENT,
            ORGANIZER_INVITE_USERS,
            ORGANIZER_GRANT_PERMISSION,
            ORGANIZER_DELETE_USERS)
    ),
    CO_ADMIN(Set.of()),
    CO_ORGANIZER(Set.of()),
    ATTENDEE(Set.of());

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }

}
