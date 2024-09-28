package com.maju_mundur.MajuMundur.dto.Request;

import com.maju_mundur.MajuMundur.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {
    private String name;
    private String phone;
    private String email;
    private Double points;
    private User user;
}
