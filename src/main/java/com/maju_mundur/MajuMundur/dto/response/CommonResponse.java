package com.maju_mundur.MajuMundur.dto.response;

import lombok.Builder;

@Builder
public class CommonResponse <T>{
    Integer statusCode;
    String message;
    T data;
    PaginationResponse paginationResponse;
}
