package com.bexos.granular_permission.dto;

import lombok.Builder;

@Builder
public record ExceptionResponse(
        String message,
        int status
) {
}
