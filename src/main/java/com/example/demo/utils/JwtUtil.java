package com.example.demo.utils;

import com.example.demo.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key key = Keys.hmacShaKeyFor(
            "hiT0VmdgqvztJVlbp6qblwAVHJzaC3GxBO3HTz1iZV1".getBytes(StandardCharsets.UTF_8)
    );
    public String generateToken(User user){
        long expirationMillis = 1000 *60 *60 * 24;
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();


    }
}
