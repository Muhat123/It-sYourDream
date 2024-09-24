package com.maju_mundur.MajuMundur.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantRequest {
    private String name;
    private String email;
    private String phone;
}
