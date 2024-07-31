package com.pulse.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pulse.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JWTService {

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;
    @Value("${jwt.life-time.access-token}")
    private long jwtAccessTokenLifeTime;
    @Value("${jwt.life-time.refresh-token}")
    private long jwtRefreshTokenLifeTime;

    private Algorithm getAlgorithm(){
        return Algorithm.HMAC256(jwtSecretKey);
    }

    public String extractEmail(String jwt){
        JWTVerifier jwtVerifier = JWT.require(getAlgorithm()).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
        return decodedJWT.getSubject();
    }

    public Long extractId(String jwt){
        JWTVerifier jwtVerifier = JWT.require(getAlgorithm()).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
        return decodedJWT.getClaim("id").asLong();
    }

    public void setAuthenticationInContext(UserDetails userDetails){
        SecurityUser securityUser = (SecurityUser) userDetails;
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        securityUser,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public String generateAccessToken(UserDetails userDetails){
        SecurityUser securityUser = (SecurityUser) userDetails;
        return JWT.create()
                .withClaim("id", securityUser.getUser().getId())
                .withClaim("firstName", securityUser.getUser().getFirstName())
                .withClaim("lastName", securityUser.getUser().getLastName())
                .withClaim("image", securityUser.getUser().getImage())
                .withClaim("status", securityUser.getUser().getStatus().toString())
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtAccessTokenLifeTime * 60 * 1000))
                .withNotBefore(new Date(System.currentTimeMillis()))
                .withClaim("authorities", userDetails.getAuthorities().stream().map(
                        GrantedAuthority::getAuthority
                ).toList())
                .sign(getAlgorithm());
    }

    public String generateRefreshToken(UserDetails userDetails){
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtRefreshTokenLifeTime * 60 * 1000))
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(getAlgorithm());
    }
}
