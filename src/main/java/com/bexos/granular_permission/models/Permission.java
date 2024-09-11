package com.bexos.granular_permission.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    ADMIN_GRANT_PERMISSION("admin_grant_permissions"),
    ADMIN_INVITE_USER("admin:invite_users"),
    ADMIN_VIEW_USERS("admin:view_users"),
    ADMIN_DELETE_USERS("admin:delete_users"),
    CO_ADMIN_INVITE_USER("co_admin:invite_users"),
    CO_ADMIN_VIEW_USERS("co_admin:view_users"),
    CO_ADMIN_DELETE_USERS("co_admin:delete_users"),
    ORGANIZER_GRANT_PERMISSION("organizer_grant_permissions"),
    ORGANIZER_CREATE_EVENT("organizer:create_event"),
    ORGANIZER_VIEW_EVENT("organizer:view_event"),
    ORGANIZER_EDIT_EVENT("organizer:edit_event"),
    ORGANIZER_DELETE_EVENT("organizer:delete_event"),
    ORGANIZER_INVITE_USERS("organizer:invite_users"),
    ORGANIZER_DELETE_USERS("organizer:delete_users"),
    CO_ORGANIZER_CREATE_EVENT("co_organizer:create_event"),
    CO_ORGANIZER_EDIT_EVENT("co_organizer:edit_event"),
    CO_ORGANIZER_DELETE_EVENT("co_organizer:delete_event"),
    CO_ORGANIZER_INVITE_USERS("co_organizer:invite_users");

    private final String permission;
}
