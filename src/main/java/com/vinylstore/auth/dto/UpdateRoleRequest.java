package com.vinylstore.auth.dto;

import com.vinylstore.auth.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRoleRequest {
    @NotNull(message = "El rol es obligatorio")
    private User.Role role;
}

