package com.maju_mundur.MajuMundur.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthTokenFilter authTokenFilter;

    private final String[] WHITE_LIST_URL = {
            "/api/auth/**", "/api/products/**","/v3/api-docs/**","/api/auth/login",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .cors(cors -> {
                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    CorsConfiguration config = new CorsConfiguration();
                    config.addAllowedOrigin("*");
                    config.addAllowedMethod("*");
                    config.addAllowedHeader("*");
                    source.registerCorsConfiguration("/**", config);
                    cors.configurationSource(source);
                })

                .httpBasic(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/product/allproducts").permitAll()
                        .requestMatchers("/api/reward/all").permitAll()
                        .requestMatchers("/api/reward/{rewardId}").permitAll()
                        .requestMatchers("/api/merchant/allmerchants").permitAll()
                        .requestMatchers("/api/merchant/{merchantId}").permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
