package com.maju_mundur.MajuMundur.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.maju_mundur.MajuMundur.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JWTUtils jwtUtils;
    private final UserService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = null;

            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring(7);
            }

            if (token != null && jwtUtils.verifyToken(token)){
                Map<String, String> userInfo = jwtUtils.getUserInfo(token);
                UserDetails user = userService.loadUserById(userInfo.get("userId"));

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                request.setAttribute("userId", userInfo.get("userId"));


            }


        } catch (JWTVerificationException e) {
            SecurityContextHolder.clearContext();
            throw new RuntimeException( "Authentication Failure : " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
