package com.maju_mundur.MajuMundur.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ImageResponse {
    private String contentType;
    private String name;
    private Long size;
    private String path;
}
