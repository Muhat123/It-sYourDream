package com.maju_mundur.MajuMundur.dto.Response;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantResponse {
    private String id;
    private String name;
    private String email;
    private String phone;
}
