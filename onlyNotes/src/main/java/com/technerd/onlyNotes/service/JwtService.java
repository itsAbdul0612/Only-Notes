package com.technerd.onlyNotes.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token){
        Claims claims = extractAllClaims(token);
        return claims.getSubject();      // Subject = Username,Email,etc.
    }

    private Date getExpiration(String token){
        return extractAllClaims(token).getExpiration();
    }

    private String createToken(Map<String,Object> claims, String subject){
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .header().empty().add("typ","JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token){
        return !isTokenExpired(token);
    }
}

