package com.maju_mundur.MajuMundur.service.impl;

import com.maju_mundur.MajuMundur.dto.Request.CustomerRequest;
import com.maju_mundur.MajuMundur.dto.Request.LoginRequest;
import com.maju_mundur.MajuMundur.dto.Request.MerchantRequest;
import com.maju_mundur.MajuMundur.dto.Request.RegisterRequest;
import com.maju_mundur.MajuMundur.dto.Response.LoginResponse;
import com.maju_mundur.MajuMundur.dto.Response.RegisterResponse;
import com.maju_mundur.MajuMundur.entity.Role;
import com.maju_mundur.MajuMundur.entity.User;
import com.maju_mundur.MajuMundur.exception.OurException;
import com.maju_mundur.MajuMundur.repository.UserRepository;
import com.maju_mundur.MajuMundur.security.JWTUtils;
import com.maju_mundur.MajuMundur.service.AuthService;
import com.maju_mundur.MajuMundur.service.CustomerService;
import com.maju_mundur.MajuMundur.service.MerchantService;
import com.maju_mundur.MajuMundur.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ComponentScan
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final CustomerService customerService;
    private final MerchantService merchantService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtil;
    private final RoleService roleService;

    @Override
    public RegisterResponse registerCustomer(RegisterRequest<CustomerRequest> registerRequest) {
        Role role = roleService.getOrSave(Role.UserRole.CUSTOMER);

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .build();
        user = userRepository.saveAndFlush(user);
        System.out.println("User ID setelah penyimpanan: " + user.getId());
        CustomerRequest customerRequest = registerRequest.getData().orElseThrow(
                () -> new OurException("Customer Request Data Not Found")
        );
        CustomerRequest customer = CustomerRequest.builder()
                .email(registerRequest.getEmail())
                .name(customerRequest.getName())
                .user(user)
                .phone(customerRequest.getPhone())
                .points(0.0)
                .build();
        customerService.create(customer, user);
        return RegisterResponse.builder()
                .username(user.getUsername())
                .role(user.getRole().getName())
                .build();
    }

    @Override
    public RegisterResponse registerMerchant(RegisterRequest<MerchantRequest> registerRequest) {
        Role role = roleService.getOrSave(Role.UserRole.MERCHANT);

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .build();
        user = userRepository.saveAndFlush(user);
        System.out.println("User ID setelah penyimpanan: " + user.getId());
        MerchantRequest customerRequest = registerRequest.getData().orElseThrow(
                () -> new OurException("Customer Request Data Not Found")
        );
        MerchantRequest customer = MerchantRequest.builder()
                .email(registerRequest.getEmail())
                .name(customerRequest.getName())
                .user(user)
                .phone(customerRequest.getPhone())
                .build();
        merchantService.create(customer, user);
        return RegisterResponse.builder()
                .username(user.getUsername())
                .role(user.getRole().getName())
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername().toLowerCase(),
                            request.getPassword()
                    )
            );
            System.out.println("User authenticated: " + authentication.getName());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();

            String token = jwtUtil.generateToken(user);

            return LoginResponse.builder()
                    .token(token)
                    .role(user.getRole().getName())
                    .build();
        } catch (BadCredentialsException e) {
            System.out.println("Login failed for user: " + request.getUsername());
            System.out.println("Error: " + e.getMessage());
            throw new OurException("Username or Password Invalid");
        }
    }



}
