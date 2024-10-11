package com.products.products.service;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTUtils {
    @Value("${security.jwt.secret-key}")
    private String secretKey;
    @Getter
    private final long jwtExpiration =86400000;

    private Claims extractAllClaims(String token) {
        return null;
    }

    public <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims =extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUserNameFromToken(String token){
        return extractClaims(token, Claims::getSubject);
    }
    public String  generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }
    public String generateToken(Map<String,Object> extraClaims,UserDetails userDetails){
        return buildToken(extraClaims,userDetails,jwtExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails,long jwtExpiration) {
        Map<String,Object> extraClaims,
        UserDetails userDetails,
        long expiration

    }


}
