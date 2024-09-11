package com.bexos.granular_permission.dto;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Builder
public record UserResponse(
        Integer id,
        String email,
        String username,
        String manager,
        String referrer,
        Collection<? extends GrantedAuthority> authorities
) {
}
