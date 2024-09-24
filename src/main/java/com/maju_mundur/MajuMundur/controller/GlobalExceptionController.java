package com.maju_mundur.MajuMundur.controller;

import com.maju_mundur.MajuMundur.dto.response.CommonResponse;
import com.maju_mundur.MajuMundur.util.AuthenticationExeption;
import com.maju_mundur.MajuMundur.util.FileStorageException;
import com.maju_mundur.MajuMundur.util.ResourceNotFoundExeption;
import com.maju_mundur.MajuMundur.util.ValidationExeption;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler({ResourceNotFoundExeption.class})
    public ResponseEntity<CommonResponse<String>> handleResourceNotFoundExeption(ResourceNotFoundExeption ex) {
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler({ValidationExeption.class})
    public ResponseEntity<CommonResponse<String>> handleValidationExeption(ValidationExeption ex) {
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
    }

    @ExceptionHandler({AuthenticationExeption.class})
    public ResponseEntity<CommonResponse<String>> handleAuthenticationExeption(AuthenticationExeption ex) {
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler({FileStorageException.class})
    public ResponseEntity<CommonResponse<String>> handleFileStorageExeption(FileStorageException ex) {
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
