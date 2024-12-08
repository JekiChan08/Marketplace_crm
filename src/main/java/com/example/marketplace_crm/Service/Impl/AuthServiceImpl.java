package com.example.marketplace_crm.Service.Impl;

import com.example.marketplace_crm.Config.EncoderConfig;
import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.Service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.security.auth.message.AuthException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl {
    private final UserService us;

    private final EncoderConfig encoderConfig;
    private final Map<String, String> refreshStorage = new HashMap<>();
    public AuthServiceImpl(UserService us, EncoderConfig sc) {
        this.us = us;
        this.encoderConfig = sc;
    }

}
