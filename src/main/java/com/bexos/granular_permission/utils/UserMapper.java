package com.bexos.granular_permission.utils;

import com.bexos.granular_permission.dto.UserResponse;
import com.bexos.granular_permission.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        System.out.println("mapper");
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .manager(user.getManager() != null ? String.valueOf(user.getManager()) : null)
                .referrer(user.getReferrer() != null ? String.valueOf(user.getReferrer()) : null)
//                .authorities(user.getAuthorities())
                .build();
    }
}
