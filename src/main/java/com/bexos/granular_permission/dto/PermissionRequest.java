package com.bexos.granular_permission.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PermissionRequest(
        @NotNull(message = "User id is required")
        Integer userId,
        @Pattern(regexp = "CO_ADMIN_INVITE_USER|CO_ADMIN_VIEW_USERS|CO_ADMIN_DELETE_USERS|CO_ORGANIZER_CREATE_EVENT|CO_ORGANIZER_EDIT_EVENT|CO_ORGANIZER_DELETE_EVENT|CO_ORGANIZER_INVITE_USERS", message = "Invalid permission")
        String permission
) {
}
