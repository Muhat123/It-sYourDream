package com.maju_mundur.MajuMundur.dto.Response;

import com.maju_mundur.MajuMundur.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class RegisterResponse {
    private String username;
    private Role.UserRole role;
}
