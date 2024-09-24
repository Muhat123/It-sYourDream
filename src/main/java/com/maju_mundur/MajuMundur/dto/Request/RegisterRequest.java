package com.maju_mundur.MajuMundur.dto.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest<T> {
    private String email;
    private String username;
    private String password;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Optional<T> data;
}
