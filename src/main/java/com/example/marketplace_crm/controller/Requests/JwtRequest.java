package com.example.marketplace_crm.controller.Requests;

import lombok.Data;

@Data
public class JwtRequest {
    private String login;
    private String password;
}
