package com.maju_mundur.MajuMundur.dto.Request;
import com.maju_mundur.MajuMundur.entity.User;
import lombok.AllArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantRequest {
    private String id;
    private String name;
    private String email;
    private String phone;
    private User user;
}
