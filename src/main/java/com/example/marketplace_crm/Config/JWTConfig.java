package com.example.marketplace_crm.Config;

import com.example.marketplace_crm.Model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
public class JWTConfig {
    private final SecretKey accessSecret;
    private final SecretKey refreshSecret;

    public JWTConfig(@Value("${jwt.secret.access}") String jwtAccess, @Value("${jwt.secret.refresh}") String jwtRefresh) {
        this.accessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccess));
        this.refreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefresh));
    }

    public String generateAccessToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        Date expirationInstant = Date.from(now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(user.getLogin())
                .expiration(expirationInstant)
                .signWith(accessSecret)
                .claim("roles", user.getRoles())
                .compact();
    }

    public String generateRefreshToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        Date expirationInstant = Date.from(now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(user.getLogin())
                .expiration(expirationInstant)
                .signWith(refreshSecret)
                .compact();
    }
    public boolean validateAccess(String accessToken){
        return validateToken(accessToken, accessSecret);
    }
    public boolean validateRefresh(String refreshToken){
        return validateToken(refreshToken, refreshSecret);
    }
    public Claims parseClaimsRefresh(String refreshToken){
        return getClaims(refreshToken, refreshSecret);
    }
    public Claims parseClaimsAccess(String accessToken){
        return getClaims(accessToken, accessSecret);
    }
    public String getLoginFromAccess(String accessToken){
        return getLogin(accessToken, accessSecret);
    }
    private boolean validateToken(String token, SecretKey secret){
        try {
            Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e){
            log.error(e.getMessage());
        }
        return false;
    }
    private Claims getClaims(String token, SecretKey secret){
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private String getLogin(String token, SecretKey secret){
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
