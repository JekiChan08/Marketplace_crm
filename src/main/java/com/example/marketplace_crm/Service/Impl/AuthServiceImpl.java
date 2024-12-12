package com.example.marketplace_crm.Service.Impl;

import com.example.marketplace_crm.Config.EncoderConfig;
import com.example.marketplace_crm.Config.JWTAuth;
import com.example.marketplace_crm.Config.JWTConfig;
import com.example.marketplace_crm.Config.SecurityConfig;
import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.controller.Requests.AccessRequest;
import com.example.marketplace_crm.controller.Requests.JwtRequest;
import com.example.marketplace_crm.controller.Responses.JwtResponse;
import com.example.marketplace_crm.Service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.security.auth.message.AuthException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl {
    private final UserService us;
    private final JWTConfig config;

    private final EncoderConfig encoderConfig;
    private final Map<String, String> refreshStorage = new HashMap<>();
    public AuthServiceImpl(UserService us, JWTConfig config, EncoderConfig sc) {
        this.us = us;
        this.config = config;
        this.encoderConfig = sc;
    }
    public JwtResponse login(JwtRequest authRequest) throws AuthException {
        User user = us.findByLogin(authRequest.getLogin());
        if(encoderConfig.bCryptPasswordEncoder().matches(authRequest.getPassword(), user.getPassword())){
            String accessToken = config.generateAccessToken(user);
            String refreshToken = config.generateRefreshToken(user);
            refreshStorage.put(user.getLogin(), refreshToken);
            return new JwtResponse(accessToken);
        }else {
            throw new AuthException("Неправильная пара логин/пароль");
        }
    }
    public JwtResponse refresh(AccessRequest request) throws Exception {
        String login = config.getLoginFromAccess(request.getAccessToken());
        if(refreshStorage.containsKey(login)){
            String refreshToken = refreshStorage.get(login);
            if (config.validateRefresh(refreshToken)){
                User user = us.findByLogin(login);
                String accessToken = config.generateAccessToken(user);
                return new JwtResponse(accessToken);
            } else {
                throw new JwtException("Невалидный рефреш токен");
            }
        } else {
            throw new Exception("Несуществующий пользователь");
        }
    }
    public JWTAuth getAuthInfo(){
        return (JWTAuth) SecurityContextHolder.getContext().getAuthentication();
    }
}
