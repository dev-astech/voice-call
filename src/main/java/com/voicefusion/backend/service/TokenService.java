// ======================== TOKEN SERVICE ========================
package com.voicefusion.backend.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${getstream.api-secret}")
    private String getStreamApiSecret;

    public Map<String, String> generateTokens(String userId) {
        // Generate Auth Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userId);

        String authToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        // Generate GetStream Token
        Map<String, Object> streamClaims = new HashMap<>();
        streamClaims.put("user_id", userId);

        String getStreamToken = Jwts.builder()
                .setClaims(streamClaims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h
                .signWith(Keys.hmacShaKeyFor(getStreamApiSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        Map<String, String> tokens = new HashMap<>();
        tokens.put("authToken", authToken);
        tokens.put("getStreamToken", getStreamToken);
        return tokens;
    }
}
