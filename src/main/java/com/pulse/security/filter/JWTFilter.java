package com.pulse.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pulse.exception.custom.CustomException;
import com.pulse.repository.UserRepository;
import com.pulse.security.SecurityUser;
import com.pulse.security.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@AllArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private JWTService jwtService;
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
    throws ServletException, IOException {

        String header =  request.getHeader("Authorization");
        String jwt = null;
        Long id = null;
        if(header == null || !header.startsWith("Bearer ") || request.getServletPath().startsWith("/auth")){
            filterChain.doFilter(request,response);
            return;
        }
        jwt = header.substring(7);
        try{
            id = jwtService.extractId(jwt);
        }catch (Exception e){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            new ObjectMapper().writeValue(response.getOutputStream(), Map.of("message",e.getMessage()));
            return;
        }
        UserDetails userDetails = new SecurityUser(
                userRepository.findById(id).orElseThrow(() -> new CustomException("User not found"))
        );
        jwtService.setAuthenticationInContext(userDetails);
        filterChain.doFilter(request,response);
    }
}
